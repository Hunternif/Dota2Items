package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

public class GuiManaBar extends Gui {
	private static final int HIGHLIGHT_TIME = 10; // [ticks]
	
	private Minecraft mc;
	private int prevMana = 0;
	private long lastChange = 0;
	
	public GuiManaBar(Minecraft mc) {
		this.mc = mc;
	}
	
	@ForgeSubscribe
	public void onRenderExperienceBar(RenderGameOverlayEvent event) {
		// Only interested in Post-ExperienceBar events (the end of overlay rendering)
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE || mc.thePlayer.capabilities.isCreativeMode) {
			return;
		}
		EntityStats stats = Dota2Items.mechanics.getEntityStats(mc.thePlayer);
		if (stats.getMaxMana() == 0) {
			return;
		}
		// Let's call the 10 discrete mana units on screen "drops".
		float halfDrop = (float)stats.getMaxMana() / 20f;
		int mana = MathHelper.floor_float(stats.getFloatMana() / halfDrop);
		long ticksSinceLastChange = mc.thePlayer.ticksExisted - lastChange;
		boolean highlight = ticksSinceLastChange <= HIGHLIGHT_TIME && ticksSinceLastChange / 3 % 2 == 1;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/mana.png");
		
		ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();

		boolean renderArmor = ForgeHooks.getTotalArmorValue(mc.thePlayer) > 0;
		int left = width / 2 - 91;
		int top = height - 49 - (renderArmor ? 10 : 0);

		int regen = -1;

		for (int i = 0; i < 10; ++i) {
			int idx = i * 2 + 1;

			int x = left + i * 8;
			int y = top;
			if (i == regen) {
				y -= 2;
			}

			drawTexturedModalRect(x, y, (highlight ? 9 : 0), 0, 9, 9);

			if (highlight) {
				if (idx < prevMana) {
					drawTexturedModalRect(x, y, 54, 0, 9, 9);
				} else if (idx == prevMana) {
					drawTexturedModalRect(x, y, 63, 0, 9, 9);
				}
			}

			if (idx < mana) {
				drawTexturedModalRect(x, y, 36, 0, 9, 9);
			} else if (idx == mana) {
				drawTexturedModalRect(x, y, 45, 0, 9, 9);
			}
		}
		
		if (prevMana != mana) {
			lastChange = mc.thePlayer.ticksExisted;
		}
		prevMana = mana;
	}
}
