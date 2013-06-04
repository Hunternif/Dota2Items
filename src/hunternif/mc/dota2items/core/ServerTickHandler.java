package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ServerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		//FIXME Make separate Mechanics instances for client and server even in singleplayer. 
		Dota2Items.mechanics.updateAllEntityStats();
		Dota2Items.mechanics.updatePlayerInventories(false);
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
