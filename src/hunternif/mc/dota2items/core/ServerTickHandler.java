package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

public class ServerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) { 
		Dota2Items.mechanics.updateAllEntityStats(Side.SERVER);
		Dota2Items.mechanics.updatePlayerInventories(Side.SERVER);
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Items Server tick handler";
	}

}
