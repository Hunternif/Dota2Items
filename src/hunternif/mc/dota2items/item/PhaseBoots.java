package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PhaseBoots extends CooldownItem {
	private static final UUID uuid = UUID.fromString("4512aab0-1ba2-11e3-b773-0800200c9a66");
	
	public static final float duration = 4f;
	
	public PhaseBoots(int id) {
		super(id);
		setCooldown(8);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!tryUse(stack, player)) {
			return stack;
		}
		startCooldown(stack, player);
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		long startTime = world.getTotalWorldTime();
		long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
		stats.addBuff(new BuffInstance(Buff.phase, player.entityId, startTime, endTime, true));
		// Make the player resistant to knockback, since he can move through units already:
		addKnockbackResistance(player);
		player.playSound(Sound.PHASE_BOOTS.getName(), 0.7f, 1);
		return stack;
	}
	
	public static void addKnockbackResistance(EntityLivingBase entity) {
		AttributeInstance attr = entity.func_110148_a(SharedMonsterAttributes.field_111266_c);
		// Get the modifier:
		AttributeModifier modifier = attr.func_111127_a(uuid);
		if (modifier != null) {
			return;
		}
		// I think the argument "0" stands for operation "add amount":
		modifier = new AttributeModifier(uuid, "Speed bonus from Dota 2 Items", 1, 0)
			.func_111168_a(false); // I think this makes it non-persistent
		attr.func_111121_a(modifier);
	}
	
	public static void removeKnockbackResistance(EntityLivingBase entity) {
		AttributeInstance attr = entity.func_110148_a(SharedMonsterAttributes.field_111266_c);
		// Get the modifier:
		AttributeModifier modifier = attr.func_111127_a(uuid);
		if (modifier != null) {
			attr.func_111124_b(modifier);
		}
	}
}
