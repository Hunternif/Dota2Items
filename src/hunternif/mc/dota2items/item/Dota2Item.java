package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.mechanics.Dota2EntityStats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Dota2Item extends Item {
	private boolean dropsOnDeath = false;
	
	public float directAttackBonusRanged = 0;
	public float directAttackBonusMelee = 0;
	public float directArmorBonus = 0;
	public float percentAttackBonusRanged = 0;
	public float percentAttackBonusMelee = 0;
	public float percentArmorBonus = 0;
	
	public float moveSpeedFactor = 1;
	private Map<Integer /*player entity ID*/, Boolean> appliedSpeedFactors = new ConcurrentHashMap<Integer, Boolean>();
	
	public Dota2Item(int id) {
		super(id);
		setCreativeTab(Dota2Items.dota2CreativeTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Dota2Items.ID + ":" + getUnlocalizedName().substring("item.".length()));
	}
	
	public static void playDenyGeneralSound(World world) {
		if (world.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Dota2ItemSounds.DENY_GENERAL, 1.0F, 1.0F);
		}
	}
	
	public void setDropsOnDeath(boolean value) {
		dropsOnDeath = value;
	}
	public boolean dropsOnDeath() {
		return dropsOnDeath;
	}

	
	public boolean hasDirectAttackBonus() {
		return directAttackBonusRanged != 0 || directAttackBonusMelee != 0;
	}
	public boolean hasDirectArmorBonus() {
		return directArmorBonus != 0;
	}
	public boolean hasPercentAttackBonus() {
		return percentAttackBonusRanged != 0 || percentAttackBonusMelee != 0;
	}
	public boolean hasPercentArmorBonus() {
		return percentArmorBonus != 0;
	}
	
	public float applyDirectAttackBonus(float baseAttack, boolean isRanged) {
		if (isRanged)
			return baseAttack + directAttackBonusRanged;
		else
			return baseAttack + directAttackBonusMelee;
	}
	public float applyDirectArmorBonus(float baseArmor) {
		return baseArmor + directArmorBonus;
	}
	
	public float applyPercentAttackBonus(float baseAttack, boolean isRanged) {
		if (isRanged)
			return baseAttack * (100f + percentAttackBonusRanged) / 100f;
		else
			return baseAttack * (100f + percentAttackBonusMelee) / 100f;
	}
	public float applyPercentArmorBonus(float baseArmor) {
		return baseArmor * (100f + percentArmorBonus) / 100f;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isItemHeld) {
		super.onUpdate(stack, world, entity, slot, isItemHeld);
		
		// Walk speed reset
		/*if (world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.capabilities.func_82877_b(0.1f);
		}*/
		
		//TODO this might conflict with EntityMotionSpeed!
		//TODO restore walk speed when the item is dropped or destroyed.
		// Cosider using GameRegistry.onItemCrafted() - it probably is called every time the player clicks a slot.
		if (world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			Boolean applied = appliedSpeedFactors.get(player.entityId);
			if (applied == null) applied = false;
			if (slot >= 0 && slot < 9) {
				if (!applied) {
					player.capabilities.setPlayerWalkSpeed(player.capabilities.getWalkSpeed() * moveSpeedFactor);
					//System.out.println(player.capabilities.getWalkSpeed());
					applied = true;
				}
			} else if (applied) {
				player.capabilities.setPlayerWalkSpeed(player.capabilities.getWalkSpeed() / moveSpeedFactor);
				//System.out.println(player.capabilities.getWalkSpeed());
				applied = false;
			}
			appliedSpeedFactors.put(player.entityId, applied);
		}
	}
	
	/*@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			Boolean applied = appliedSpeedFactors.get(player.entityId);
			if (applied == null) applied = false;
			if (applied) {
				player.capabilities.func_82877_b(player.capabilities.getWalkSpeed() / moveSpeedFactor);
				System.out.println(player.capabilities.getWalkSpeed());
				applied = false;
			}
			appliedSpeedFactors.put(player.entityId, applied);
		}
		return true;
	}*/
	
	public boolean canUseItem(Entity player) {
		Dota2EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		if (stats == null) {
			return true;
		} else {
			return stats.canUseItems();
		}
	}
}
