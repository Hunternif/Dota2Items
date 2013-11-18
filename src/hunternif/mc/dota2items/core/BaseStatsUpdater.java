package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/** Handles base stats, movement speed, mana&HP regen. */
public class BaseStatsUpdater implements IEntityUpdater {
	/** I have no idea how to generate these properly. */
	private static final UUID uuid = UUID.fromString("92f7a640-0ac7-11e3-8ffd-0800200c9a66");
	
	public static final int FOOD_THRESHOLD_FOR_HEAL = 0;
	public static final float STR_PER_LEVEL = 2;
	public static final float AGI_PER_LEVEL = 2;
	public static final float INT_PER_LEVEL = 2;
	
	/** In seconds. */
	public static final float SYNC_STATS_INTERVAL = 10;
	
	@Override
	public void update(EntityLivingBase entity, EntityStats stats, LivingUpdateEvent event) {
		if (entity instanceof EntityPlayer) {
			// Regenerate health and mana every second:
			regenHealthAndMana(stats);
			// Add base attributes per level:
			updateBaseAttributes((EntityPlayer)entity, stats);
			// Synchronize stats with all clients every SYNC_STATS_INTERVAL seconds:
			int time = entity.ticksExisted;
			if (!entity.worldObj.isRemote && time - stats.lastSyncTime >=
					(long) (MCConstants.TICKS_PER_SECOND * SYNC_STATS_INTERVAL)) {
				stats.sendSyncPacketToClient((EntityPlayer)entity);
			}
		}
		stats.clampMana();
		for (BuffInstance buffInst : stats.getAppliedBuffs()) {
			if (!buffInst.isPermanent() && entity.worldObj.getTotalWorldTime() > buffInst.endTime) {
				stats.removeBuff(buffInst);
			}
		}
		updateMoveSpeed(entity, stats);
		if (!stats.canMove()) {
			event.setCanceled(true);
			// Update items in inventory so that cooldown keeps on ticking:
			if (entity instanceof EntityPlayer) {
				((EntityPlayer)entity).inventory.decrementAnimations();
			}
		}
	}
	
	private void updateMoveSpeed(EntityLivingBase entity, EntityStats stats) {
		AttributeInstance moveSpeedAttribute = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		double newMoveSpeed = stats.getMovementSpeed();
		double oldMoveSpeed = moveSpeedAttribute.getAttributeValue();
		if (newMoveSpeed != oldMoveSpeed) {
			double baseMoveSpeed = moveSpeedAttribute.getBaseValue();
			// Get the modifier:
			AttributeModifier speedModifier = moveSpeedAttribute.getModifier(uuid);
			if (speedModifier != null) {
				// Remove the old modifier
				moveSpeedAttribute.removeModifier(speedModifier);
			}
			// I think the argument "2" stands for operation "add percentage":
			speedModifier = new AttributeModifier(uuid, "Speed bonus from Dota 2 Items", newMoveSpeed / baseMoveSpeed - 1.0, 2)
				.setSaved(false); // I think this makes it non-persistent
			moveSpeedAttribute.applyModifier(speedModifier);
		}
	}
	
	private static void regenHealthAndMana(EntityStats stats) {
		if (shouldHeal(stats)) {
			stats.heal(stats.getHealthRegen() / MCConstants.TICKS_PER_SECOND);
		}
		if ((stats.entity instanceof EntityPlayer) && ((EntityPlayer)stats.entity).capabilities.isCreativeMode) {
			stats.setMana(stats.getMaxMana());
		} else {
			if (stats.entity.getHealth() > 0 && stats.getMana() < stats.getMaxMana()) {
				stats.addMana(stats.getManaRegen() / MCConstants.TICKS_PER_SECOND);
			}
		}
	}
	
	public static boolean shouldHeal(EntityStats stats) {
		int health = stats.getHealth(stats.entity);
		int maxHealth = stats.getMaxHealth();
		boolean shouldHeal = health > 0 && health < maxHealth;
		if (stats.entity instanceof EntityPlayer) {
			shouldHeal &= ((EntityPlayer)stats.entity).getFoodStats().getFoodLevel() >= FOOD_THRESHOLD_FOR_HEAL;
		}
		return shouldHeal;
	}
	
	private static void updateBaseAttributes(EntityPlayer player, EntityStats stats) {
		stats.setBaseStrength(EntityStats.BASE_PLAYER_STR + player.experienceLevel * STR_PER_LEVEL);
		stats.setBaseAgility(EntityStats.BASE_PLAYER_AGI + player.experienceLevel * AGI_PER_LEVEL);
		stats.setBaseIntelligence(EntityStats.BASE_PLAYER_INT + player.experienceLevel * INT_PER_LEVEL);
	}

}
