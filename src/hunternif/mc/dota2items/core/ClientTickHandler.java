package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

public class ClientTickHandler implements ITickHandler {
	public static Timer timer = new Timer(MCConstants.TICKS_PER_SECOND);

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.CLIENT)) {
			Dota2Items.mechanics.checkAndUpdatePlayerInventories(Side.CLIENT);
			Dota2Items.mechanics.updateAllEntityStats(Side.CLIENT);
			ClientProxy.swingRenderer.onTick();
		}
		if (type.contains(TickType.RENDER)) {
			timer.updateTimer();
			if (Minecraft.getMinecraft().inGameHasFocus) {
				if (Minecraft.getMinecraft().thePlayer.isSwingInProgress &&
						!ClientProxy.swingRenderer.isSwinging) {
					ClientProxy.swingRenderer.startSwinging();
				}
				ClientProxy.swingRenderer.onRender((float)tickData[0]);
			}
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
