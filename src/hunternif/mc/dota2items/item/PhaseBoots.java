package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.event.BuffEvent.BuffAddEvent;
import hunternif.mc.dota2items.event.BuffEvent.BuffRemoveEvent;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

public class PhaseBoots extends ActiveItem {
	private static final UUID uuid = UUID.fromString("4512aab0-1ba2-11e3-b773-0800200c9a66");
	
	public static final float duration = 4f;
	
	public PhaseBoots(int id) {
		super(id);
		setCooldown(8);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void onUse(ItemStack stack, EntityPlayer player) {
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		long startTime = player.worldObj.getTotalWorldTime();
		long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
		stats.addBuff(new BuffInstance(Buff.phase, player.entityId, startTime, endTime, true));
		player.playSound(Sound.PHASE_BOOTS.getName(), 0.7f, 1);
	}
	
	@ForgeSubscribe
	public void onBuffAdd(BuffAddEvent event) {
		if (event.buffInst.buff == Buff.phase) {
			// Make the player resistant to knockback, since he can move through units already:
			addKnockbackResistance(event.entityLiving);
		}
	}
	
	@ForgeSubscribe
	public void onBuffRemove(BuffRemoveEvent event) {
		if (event.buffInst.buff == Buff.phase) {
			removeKnockbackResistance(event.entityLiving);
		}
	}
	
	public static void addKnockbackResistance(EntityLivingBase entity) {
		AttributeInstance attr = entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
		// Get the modifier:
		AttributeModifier modifier = attr.getModifier(uuid);
		if (modifier != null) {
			return;
		}
		// I think the argument "0" stands for operation "add amount":
		modifier = new AttributeModifier(uuid, "Speed bonus from Dota 2 Items", 1, 0)
			.setSaved(false); // I think this makes it non-persistent
		attr.applyModifier(modifier);
	}
	
	public static void removeKnockbackResistance(EntityLivingBase entity) {
		AttributeInstance attr = entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
		// Get the modifier:
		AttributeModifier modifier = attr.getModifier(uuid);
		if (modifier != null) {
			attr.removeModifier(modifier);
		}
	}
}
