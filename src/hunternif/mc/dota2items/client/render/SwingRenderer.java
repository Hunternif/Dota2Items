package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class SwingRenderer {
	private static final String[] swingProgressObfFields = {"swingProgress", "aE", "field_70733_aJ"};
	
	/** From 0 to 1. */
	private float swingProgress = 0;
	private float prevSwingProgress = 0;
	
	/** In [Ticks] */
	private float swingPosition = 0;
	
	public boolean isSwinging = false;
	
	public void onTick() {
		if (Minecraft.getMinecraft().inGameHasFocus) {
			prevSwingProgress = swingProgress;
			EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats( Minecraft.getMinecraft().thePlayer );
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
	
	public void onRender(float partialTickTime) {
		if (Minecraft.getMinecraft().inGameHasFocus) {
			float dSwing = swingProgress - prevSwingProgress;
			if (dSwing < 0) {
				dSwing++; // Why?
			}
			float currentSwingProgress = prevSwingProgress + dSwing * partialTickTime;
			EntityLivingBase player = Minecraft.getMinecraft().thePlayer;
			ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, player, currentSwingProgress, swingProgressObfFields);
		}
	}
	
	public void startSwinging() {
		isSwinging = true;
		swingProgress = 0;
		swingPosition = -1;
		prevSwingProgress = 0;
	}
}
