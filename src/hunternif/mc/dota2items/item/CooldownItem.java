package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.event.CooldownEndDisplayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

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
	
	/**
	 * Reports the cooldown for the client 0.05 seconds earlier than for the server.
	 * This should eliminate the problem when the server starts cooldown before the
	 * client, and as a result the client doesn't perform the item's action.
	 */
	//TODO Fix this cooldown KLUDGE! All the checks must only be performed on the server.
	public boolean isOnSideSpecificCooldown(World world, ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		float minimum = world.isRemote ? 0.05f : 0;
		return tag != null && tag.getFloat(TAG_COOLDOWN) > minimum;
	}
	
	/**
	 * Starts cooldown on this stack for given duration in seconds.
	 * Returns false if already on cooldown.
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
	 */
	public boolean startCooldown(ItemStack itemStack, EntityPlayer player) {
		return startCooldown(itemStack, getCooldown(), player);
	}
	
	/**
	 * Plays the deny_cooldown sound on the client. On the server does nothing.
	 */
	public static void playDenyCooldownSound(World world) {
		if (world.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Dota2ItemSounds.DENY_COOLDOWN, 1.0F, 1.0F);
		}
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
				cooldown -= 0.05F;
				setRemainingCooldown(itemStack, cooldown);
				// -0.05 ensures we get a total decrement of -1 each second,
				// as there are 20 ticks in each second.
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
}
