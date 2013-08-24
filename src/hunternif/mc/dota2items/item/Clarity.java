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

public class Clarity extends Dota2Item {
	
	private static final float DURATION = 30f;
	
	public Clarity(int id) {
		super(id);
		setMaxStackSize(64);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.stackSize--;
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		long endTime = world.getTotalWorldTime() + (long) (DURATION * MCConstants.TICKS_PER_SECOND);
		stats.addBuff(new BuffInstance(Buff.clarity, player.entityId, endTime));
		player.playSound(Sound.CLARITY.getName(), 1, 1);
		return itemStack;
	}

}
