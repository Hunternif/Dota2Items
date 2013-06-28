package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;

import java.util.EnumSet;

import net.minecraft.util.Timer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

public class ClientTickHandler implements ITickHandler {
	public static Timer timer = new Timer(20);

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.CLIENT)) {
			Dota2Items.mechanics.updateAllEntityStats(Side.CLIENT);
			Dota2Items.mechanics.updatePlayerInventories(Side.CLIENT);
		}
		if (type.contains(TickType.RENDER)) {
			timer.updateTimer();
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)) {
			ClientProxy.guiGold.render();
			ClientProxy.guiStats.render();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Items Client tick handler";
	}

}
