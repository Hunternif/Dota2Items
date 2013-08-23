package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.client.event.CooldownEndDisplayEvent;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.network.UseItemPacket;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CooldownItem extends Dota2Item {
	protected static final String TAG_DURATION = "D2IcdDuration";
	protected static final String TAG_COOLDOWN = "D2IcdLeft";
	
	/**
	 * Here I place item stacks from previous update iterations. During cooldown
	 * the itemStack is constantly updated by the server, so I use this array on
	 * the client side in order to set animationsToGo on the final tick of
	 * cooldown knowing that this very item stack won't be rewritten by the
	 * server anytime soon.
	 * Phew, that was long. I need to learn to write shorter comments.
	 */
	private ItemStack[] inventoryStacks = new ItemStack[36];
	
	private float cooldown = 0;
	private int manaCost = 0;
	
	public CooldownItem(int id) {
		super(id);
	}
	
	/** Set "usual" cooldown duration in seconds. */
	public CooldownItem setCooldown(float value) {
		cooldown = Math.max(0, value);
		return this;
	}
	/** Get "usual" cooldown duration in seconds. */
	public float getCooldown() {
		return cooldown;
	}
	
	public CooldownItem setManaCost(int value) {
		manaCost = value;
		return this;
	}
	public int getManaCost() {
		return manaCost;
	}
	
	/**
	 * Get custom cooldown for this itemStack.
	 * It may or may not be the same as getCooldown()
	 */
	public float getCooldown(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag != null)
			return tag.getFloat(TAG_DURATION);
		else
			return getCooldown();
	}
	
	/** Set remaining cooldown for the itemStack in seconds. */
	public float getRemainingCooldown(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag != null)
			return tag.getFloat(TAG_COOLDOWN);
		else
			return 0;
	}
	protected void setRemainingCooldown(ItemStack itemStack, float value) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag != null)
			tag.setFloat(TAG_COOLDOWN, Math.max(0, value));
	}
	
	public boolean isOnCooldown(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		return tag != null && tag.getFloat(TAG_COOLDOWN) > 0;
	}
	
	//FIXME: "blank" uses issue is not solved yet! Must send a special packet from client to server.
	/**
	 * Starts cooldown on this stack for given duration in seconds.
	 * Returns false if already on cooldown.<br>
	 * <i>To prevent "blank" uses, only call this on the server side!</i>
	 */
	public boolean startCooldown(ItemStack itemStack, float duration, EntityPlayer player) {
		if (player.capabilities.isCreativeMode) {
			return false;
		}
		if (isOnCooldown(itemStack)) {
			return false;
		} else if (duration > 0) {
			float cooldown = duration;
			NBTTagCompound tag = itemStack.getTagCompound();
			if (tag == null) {
				tag = new NBTTagCompound();
				itemStack.setTagCompound(tag);
			}
			tag.setFloat(TAG_DURATION, duration);
			tag.setFloat(TAG_COOLDOWN, cooldown);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Starts cooldown of duration getCooldown() on this stack.
	 * Returns false if already on cooldown.<br>
	 * <i>To prevent "blank" uses, only call this on the server side!</i>
	 */
	public boolean startCooldown(ItemStack itemStack, EntityPlayer player) {
		return startCooldown(itemStack, getCooldown(), player);
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity player, int slotInInventory, boolean isItemHeld) {
		super.onUpdate(itemStack, world, player, slotInInventory, isItemHeld);
		if (player instanceof EntityPlayer && ((EntityPlayer) player).capabilities.isCreativeMode && isOnCooldown(itemStack)) {
			// If switched to Creative mode, end all cooldowns
			setRemainingCooldown(itemStack, 0);
		}
		if (isOnCooldown(itemStack) && !world.isRemote) {
			float cooldown = getRemainingCooldown(itemStack);
			if (cooldown > 0) {
				cooldown -= 1f / MCConstants.TICKS_PER_SECOND;
				setRemainingCooldown(itemStack, cooldown);
			}
		} else if (world.isRemote && slotInInventory >= 0 && slotInInventory < 36) {
			// For client-side rendering of the off-cooldown effect
			ItemStack prevInvStack = inventoryStacks[slotInInventory];
			if (prevInvStack != itemStack) {
				inventoryStacks[slotInInventory] = itemStack;
				// See javadoc comment on inventoryStacks
				// This event still may be fired twice - for last local item
				// stack and for the final item stack from the server!
				if (prevInvStack != null && isOnCooldown(prevInvStack) && !isOnCooldown(itemStack)) {
					MinecraftForge.EVENT_BUS.post(new CooldownEndDisplayEvent(slotInInventory, itemStack));
				}
			}
		}
	}
	
	public static void playDenyGeneralSound(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Sound.DENY_GENERAL.getName(), 1.0F, 1.0F);
		}
	}

	/**
	 * Returns the Sound that signifies the particular reason why the player
	 * cannot use this item.
	 */
	public Sound canUseItem(ItemStack stack, EntityPlayer player, Entity target) {
		EntityStats playerStats = Dota2Items.mechanics.getEntityStats(player);
		if (!playerStats.canUseItems()) {
			return Sound.DENY_SILENCE;
		}
		if (target != null) {
			if (!(target instanceof EntityLivingBase)) {
				return Sound.DENY_GENERAL;
			}
			EntityStats targetStats = Dota2Items.mechanics.getEntityStats((EntityLivingBase)target);
			if (targetStats.isMagicImmune()) {
				return Sound.MAGIC_IMMUNE;
			}
		}
		if (player instanceof EntityPlayer && ((EntityPlayer)player).capabilities.isCreativeMode) {
			return null;
		} else if (isOnCooldown(stack)) {
			return Sound.DENY_COOLDOWN;
		} else if (Dota2Items.mechanics.getEntityStats(player).getMana() < this.getManaCost()) {
			return Sound.DENY_MANA;
		}
		return null;
	}
	/** See {@link #canUseItem(ItemStack, EntityPlayer, Entity)} */
	public Sound canUseItem(ItemStack stack, EntityPlayer player) {
		return canUseItem(stack, player, null);
	}
	
	private Map<EntityPlayer, Set<ItemStack>> clientUsesMap = new ConcurrentHashMap<EntityPlayer, Set<ItemStack>>();
	/** Only to be called by the server. */
	public void onClientUsedItem(EntityPlayer player, ItemStack stack) {
		Set<ItemStack> usesSet = clientUsesMap.get(player);
		if (usesSet == null) {
			usesSet = Collections.synchronizedSet(new HashSet<ItemStack>());
			clientUsesMap.put(player, usesSet);
		}
		usesSet.add(stack);
	}
	private boolean didClientUse(EntityPlayer player, ItemStack stack) {
		Set<ItemStack> usesSet = clientUsesMap.get(player);
		if (usesSet == null) {
			return false;
		}
		return usesSet.contains(stack);
	}
	
	/**
	 * If the player cannot use this item, a respective sound is played.
	 * @return whether the player can use this item.
	 */
	public boolean tryUse(ItemStack stack, EntityPlayer player, Entity target) {
		if (!player.worldObj.isRemote) {
			if (didClientUse(player, stack)) {
				Set<ItemStack> usesSet = clientUsesMap.get(player);
				usesSet.remove(stack);
			} else {
				return false;
			}
		}
		Sound failSound = canUseItem(stack, player, target);
		if (player.worldObj.isRemote) {
			if (failSound == null) {
				PacketDispatcher.sendPacketToServer(new UseItemPacket().makePacket());
			} else {
				Minecraft.getMinecraft().sndManager.playSoundFX(failSound.getName(), 1.0F, 1.0F);
			}
		}
		return failSound == null;
	}
	/** See {@link #tryUse(ItemStack, EntityPlayer, Entity)} */
	public boolean tryUse(ItemStack stack, EntityPlayer player) {
		return tryUse(stack, player, null);
	}
}
