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

public class Clarity extends Dota2Item {
	
	public static final float duration = 30f;
	
	public Clarity(int id) {
		super(id);
		setMaxStackSize(64);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
		itemStack.stackSize--;
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		long startTime = world.getTotalWorldTime();
		long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
		stats.addBuff(new BuffInstance(Buff.clarity, player.entityId, startTime, endTime, true));
		player.playSound(Sound.CLARITY.getName(), 0.7f, 1);
		return itemStack;
	}

}
