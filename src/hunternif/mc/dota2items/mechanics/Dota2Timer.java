package hunternif.mc.dota2items.mechanics;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Dota2Timer implements ITickHandler {
	public List<IUpdatable> handlers = new ArrayList<IUpdatable>();
	
	public synchronized void registerHandler(IUpdatable handler) {
		handlers.add(handler);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.CLIENT)) {
			for (IUpdatable handler : handlers) {
				handler.onUpdate(true);
			}
		}
		if (type.contains(TickType.SERVER)) {
			for (IUpdatable handler : handlers) {
				handler.onUpdate(false);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Items General Tick Handler";
	}
}
