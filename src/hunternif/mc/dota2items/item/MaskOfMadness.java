package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class MaskOfMadness extends CooldownItem {
	public static final float duration = 12f;
	
	public MaskOfMadness(int id) {
		super(id);
		setCooldown(25);
		setManaCost(25);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!tryUse(stack, player)) {
			return stack;
		}
		MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
		startCooldown(stack, player);
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		stats.removeMana(getManaCost());
		long startTime = world.getTotalWorldTime();
		long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
		stats.addBuff(new BuffInstance(Buff.berserk, player.entityId, startTime, endTime, true));
		world.playSoundAtEntity(player, Sound.MASK_OF_MADNESS.getName(), 0.6f, 1);
		return stack;
	}
}
