package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.client.gui.HUD;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler {
	public static Timer timer = new Timer(MCConstants.TICKS_PER_SECOND);
	private List<HUD> guis = new ArrayList<HUD>();
	
	public void registerGui(HUD gui) {
		if (!guis.contains(gui)) {
			guis.add(gui);
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.CLIENT)) {
			ClientProxy.swingRenderer.onTick();
		}
		if (type.contains(TickType.RENDER)) {
			timer.updateTimer();
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (Minecraft.getMinecraft().thePlayer.isSwingInProgress &&
						!ClientProxy.swingRenderer.isSwinging) {
					ClientProxy.swingRenderer.startSwinging();
				}
				ClientProxy.swingRenderer.onRender((Float)tickData[0]);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)) {
			for (HUD gui : guis) {
				if (gui.shouldRender()) {
					gui.render();
				}
			}
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
