package hunternif.mc.dota2items.client;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;

/**
 * This tick handler overrides arm swing animation on the player in order to
 * make it look swifter, even when actual swing time is low.
 */
public class SwingTickHandler implements ITickHandler {
	private static final String[] swingProgressObfFields = {"swingProgress", "aE", "field_70733_aJ"};
	
	/** From 0 to 1. */
	private float swingProgress = 0;
	private float prevSwingProgress = 0;
	
	/** In [Ticks] */
	private float swingPosition = 0;
	
	public boolean isSwinging = false;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.CLIENT)) {
			onTick();
		}
		if (type.contains(TickType.RENDER)) {
			if (Minecraft.getMinecraft().thePlayer != null) {
				if (Minecraft.getMinecraft().thePlayer.isSwingInProgress && !isSwinging) {
					startSwinging();
				}
				onRender((Float)tickData[0]);
			}
		}
	}

	private void onTick() {
		if (Minecraft.getMinecraft().thePlayer != null) {
			prevSwingProgress = swingProgress;
			EntityStats stats = Dota2Items.stats.getOrCreateEntityStats( Minecraft.getMinecraft().thePlayer );
			float animationLength = stats.getAttackTime() * MCConstants.TICKS_PER_SECOND;
			if (isSwinging) {
				swingPosition++;
				if (swingPosition > animationLength) {
					swingPosition = 0;
					isSwinging = false;
				}
			} else {
				swingPosition = 0;
			}
			swingProgress = swingPosition / animationLength;
		}
	}
	
	private void onRender(float partialTickTime) {
		if (Minecraft.getMinecraft().thePlayer != null) {
			float dSwing = swingProgress - prevSwingProgress;
			if (dSwing < 0) {
				dSwing++; // Why?
			}
			float currentSwingProgress = (float)swingCurve(prevSwingProgress + dSwing * partialTickTime);
			EntityLivingBase player = Minecraft.getMinecraft().thePlayer;
			ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, player, currentSwingProgress, swingProgressObfFields);
		}
	}
	
	private double swingCurve(double x) {
		return 2*x - x*x;
	}
	
	private void startSwinging() {
		isSwinging = true;
		swingProgress = 0;
		swingPosition = -1;
		prevSwingProgress = 0;
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Items Swing tick handler";
	}
}
