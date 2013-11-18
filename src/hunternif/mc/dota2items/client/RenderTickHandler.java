package hunternif.mc.dota2items.client;

import hunternif.mc.dota2items.client.gui.HUD;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.Timer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * This tick handler updates a tick timer and renders HUDs when needed.
 */
public class RenderTickHandler implements ITickHandler {
	public static Timer timer = new Timer(MCConstants.TICKS_PER_SECOND);
	private List<HUD> guis = new ArrayList<HUD>();
	
	public void registerHUD(HUD gui) {
		if (!guis.contains(gui)) {
			guis.add(gui);
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		timer.updateTimer();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		for (HUD gui : guis) {
			if (gui.shouldRender()) {
				gui.render();
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Items Render tick handler";
	}

}
