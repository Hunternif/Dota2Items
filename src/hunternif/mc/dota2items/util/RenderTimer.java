package hunternif.mc.dota2items.util;

import hunternif.mc.dota2items.render.ISimpleRenderer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Timer;

@SideOnly(Side.CLIENT)
public class RenderTimer extends Timer implements ITickHandler {
	private List<ISimpleRenderer> postProcessors = new ArrayList<ISimpleRenderer>();
	private List<ISimpleRenderer> preProcessors = new ArrayList<ISimpleRenderer>();
	
	public RenderTimer(float ticksPerSecond) {
		super(ticksPerSecond);
	}
	
	public void registerPreProcessor(ISimpleRenderer renderer) {
		preProcessors.add(renderer);
	}
	
	public void registerPostProcessor(ISimpleRenderer renderer) {
		postProcessors.add(renderer);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)) {
			for (ISimpleRenderer renderer : preProcessors) {
				renderer.render();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.RENDER)) {
			updateTimer();
			for (ISimpleRenderer renderer : postProcessors) {
				renderer.render();
			}
		}
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Dota2Items RenderTimer";
	}
}
