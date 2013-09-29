package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.network.BuffForcePacket;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.PacketDispatcher;

public class MaskOfMadness extends ActiveItem {
	public static final float duration = 12f;
	
	public MaskOfMadness(int id) {
		super(id);
		setCooldown(25);
		setManaCost(25);
	}
	
	@Override
	protected void onUse(ItemStack stack, EntityPlayer player) {
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		long startTime = player.worldObj.getTotalWorldTime();
		long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
		BuffInstance buffInst = new BuffInstance(Buff.berserk, player.entityId, startTime, endTime, true);
		stats.addBuff(buffInst);
		PacketDispatcher.sendPacketToAllPlayers(new BuffForcePacket(buffInst).makePacket());
		player.worldObj.playSoundAtEntity(player, Sound.MASK_OF_MADNESS.getName(), 0.6f, 1);
	}
}
