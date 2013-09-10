package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PhaseBoots extends CooldownItem {
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
		player.playSound(Sound.PHASE_BOOTS.getName(), 0.7f, 1);
		return stack;
	}
}
