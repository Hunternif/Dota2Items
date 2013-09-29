package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.effect.Effect;
import hunternif.mc.dota2items.effect.EffectInstance;
import hunternif.mc.dota2items.item.BlinkDagger;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AttackHandler {
	/** Equals to Base Hero health (with base strength bonuses) over Steve's base health.
	 * This gives a zombie attack damage of 22.5~52.5. Seems fair to me. */
	public static final float DOTA_VS_MINECRAFT_DAMAGE = (float)EntityStats.BASE_PLAYER_HP / MCConstants.MINECRAFT_PLAYER_HP;
	
	@ForgeSubscribe
	public void onLivingAttack(LivingAttackEvent event) {
		if (event.source == DamageSource.outOfWorld) {
			return;
		}
		//BUG in MC: when the player is hurt, this event is posted twice!
		// Check if the entity can attack
		Entity entity = event.source.getSourceOfDamage();
		if (!(event.source instanceof EntityDamageSourceIndirect) // Actual attack has already been performed
				&& entity != null && entity instanceof EntityLivingBase) {
			EntityStats stats = Dota2Items.stats.getEntityStats(entity);
			if (stats != null) {
				long worldTime = entity.worldObj.getTotalWorldTime();
				if (stats.lastAttackTime == worldTime) {
					//See the bug notice above
					return;
				}
				boolean attackTimeoutPassed = stats.lastAttackTime +
						(long)(stats.getAttackTime() * MCConstants.TICKS_PER_SECOND) <= worldTime;
				if (stats.canAttack() && attackTimeoutPassed) {
					stats.lastAttackTime = worldTime;
				} else {
					event.setCanceled(true);
					return;
				}
			}
		}
		
		EntityStats targetStats = Dota2Items.stats.getEntityStats(event.entityLiving);
		EntityStats sourceStats = null;
		if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
			sourceStats = Dota2Items.stats.getEntityStats(event.source.getSourceOfDamage());
		}
		if (targetStats != null) {
			if (targetStats.isInvulnerable()) {
				if (Dota2Items.debug) {
					Dota2Items.logger.info("invulnerable");
				}
				event.setCanceled(true);
				return;
			} else if (event.source.isMagicDamage() && targetStats.isMagicImmune()) {
				if (Dota2Items.debug) {
					Dota2Items.logger.info("magic immune");
				}
				event.setCanceled(true);
				return;
			}
			// Try evading the attack:
			boolean trueStrike = sourceStats != null && sourceStats.isTrueStrike();
			if (targetStats.canEvade() && !trueStrike) {
				if (Dota2Items.debug) {
					Dota2Items.logger.info("evaded");
				}
				event.setCanceled(true);
				if (event.source.getSourceOfDamage() instanceof EntityLivingBase) { 
					EffectInstance effect = new EffectInstance(Effect.miss, entity.posX, entity.posY+1.5, entity.posZ);
					EffectInstance.notifyPlayersAround(effect, entity);
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onLivingHurt(LivingHurtEvent event) {
		if (event.source == DamageSource.outOfWorld) {
			return;
		}
		float dotaDamage = event.ammount * DOTA_VS_MINECRAFT_DAMAGE;
		
		// Check if the target entity is invulnerable or if damage is magical and target is magic immune
		EntityStats targetStats = Dota2Items.stats.getEntityStats(event.entityLiving);
		EntityStats sourceStats = null;
		if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
			sourceStats = Dota2Items.stats.getEntityStats(event.source.getSourceOfDamage());
		}
		
		// Apply attack bonuses to the attacker
		if (sourceStats != null) {
			dotaDamage += sourceStats.getBonusDamage();
			dotaDamage = sourceStats.getDamage(dotaDamage, !event.source.isProjectile());
			float critMultiplier = sourceStats.getCriticalMultiplier();
			if (critMultiplier > 1f) {
				if (sourceStats.entity instanceof EntityPlayer) {
					((EntityPlayer)sourceStats.entity).onCriticalHit(event.entityLiving);
				}
				if (Dota2Items.debug) {
					Dota2Items.logger.info("crit");
				}
				dotaDamage *= critMultiplier;
				sourceStats.entity.worldObj.playSoundAtEntity(sourceStats.entity, Sound.CRIT.getName(), 1, 1);
			}
			// If the player is the attacker, his target must be given EntityStats:
			if (targetStats == null && sourceStats.entity instanceof EntityPlayer) {
				targetStats = Dota2Items.stats.getOrCreateEntityStats(event.entityLiving);
				targetStats.addPlayerAttackerID(sourceStats.entity.entityId);
			}
		}
		
		if (event.source.isMagicDamage()) {
			if (targetStats != null) {
				dotaDamage *= 1f - targetStats.getSpellResistance();
				dotaDamage *= targetStats.getAmplifyDamage(true);
				//TODO test spell resistance and magic amplification.
			}
		} else {// Armor only applies to non-magical damage
			if (targetStats != null) {
				// Apply damage block:
				ItemStack targetEquippedItem = event.entityLiving.getCurrentItemOrArmor(0);
				boolean targetIsRanged = targetEquippedItem != null &&
						(targetEquippedItem.itemID == Item.bow.itemID || targetEquippedItem.itemID == Config.daedalus.getID());
				boolean isHero = event.source.getSourceOfDamage() instanceof EntityPlayer;
				dotaDamage -= targetStats.getDamageBlock(!targetIsRanged, isHero);
				if (dotaDamage < 0) dotaDamage = 0;
				
				// Apply armor bonuses to the entity being hurt
				int armor = 0;
				armor = targetStats.getArmor();
				// The formula was taken from Dota 2 Wiki
				float armorMultiplier = 1f;
				if (armor > 0) {
					armorMultiplier = 1f - ((0.06f * (float)armor) / (1 + 0.06f * (float)armor));
				} else if (armor < 0) {
					armor = Math.max(-20, armor);
					armorMultiplier = 2f - (float) Math.pow(0.94, (double) -armor);
				}
				dotaDamage *= armorMultiplier;
				dotaDamage *= targetStats.getAmplifyDamage(false);
			}
			
			// Apply lifesteal:
			if (sourceStats != null) {
				//TODO implement Unique Attack Modifiers
				float lifeStolen = dotaDamage * sourceStats.getLifestealMultiplier();
				if (lifeStolen > 0) {
					sourceStats.heal(lifeStolen);
					Entity entity = event.source.getSourceOfDamage();
					EffectInstance effect = new EffectInstance(Effect.lifesteal, entity.posX, entity.posY+1, entity.posZ);
					EffectInstance.notifyPlayersAround(effect, entity);
				}
			}
		}
		
		//--------------- Recalculate Dota damage to Minecraft -----------------
		
		// If target has bonus health, decrease damage accrodringly:
		float bonusHealthMultiplier = 1f;
		if (targetStats != null) {
			bonusHealthMultiplier = (float)targetStats.baseHealth / (float)targetStats.getMaxHealth();
		}
		dotaDamage *= bonusHealthMultiplier;
		
		float floatMCDamage = dotaDamage / DOTA_VS_MINECRAFT_DAMAGE;
		int intMCDamage = MathHelper.floor_float(floatMCDamage);
		// Store or apply the partial damage, that doesn't constitute enough to deplete 1 half-heart.
		if (targetStats != null) {
			intMCDamage = targetStats.getDamageFloor(floatMCDamage);
		}
		if (Dota2Items.debug && (event.entityLiving instanceof EntityPlayer ||
				event.source.getSourceOfDamage() instanceof EntityPlayer)) {
			Dota2Items.logger.info(String.format("Changed damage from %.2f to %.2f", event.ammount, floatMCDamage));
		}
		event.ammount = intMCDamage;
	}
	
	@ForgeSubscribe
	public void onHurt(LivingHurtEvent event) {
		// Why is this only called on the server?
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			
			// If the player is attacked by living entities, some items start cooldown:
			if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
				int invSize = player.inventory.getSizeInventory();
				for (int i = 0; i < invSize; i++) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					// Blink Dagger
					if (stack != null && stack.itemID == Config.blinkDagger.getID()) {
						float cdLeft = Config.blinkDagger.instance.getRemainingCooldown(stack);
						if (cdLeft == 0) {
							Config.blinkDagger.instance.startCooldown(stack, BlinkDagger.hurtCooldown, player);
						} else if (cdLeft < BlinkDagger.hurtCooldown) {
							Config.blinkDagger.instance.setRemainingCooldown(stack, BlinkDagger.hurtCooldown);
						}
					} // also Heart of Tarrasque
				}
			}
			
			// ... and some buffs are removed:
			Dota2Items.stats.removeBuffsOnHurt(player);
		}
	}
}
