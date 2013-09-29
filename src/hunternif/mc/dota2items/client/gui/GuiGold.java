package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGold extends Gui {
	private static final ResourceLocation texture = new ResourceLocation(Dota2Items.ID+":textures/gui/container/shop_sell.png");
	
	public static final int GUI_GOLD_WIDTH = 61;
	public static final int GUI_GOLD_HEIGHT = 23;
	public static final int GUI_INVENTORY_WIDTH = 176;
	public static final int GUI_INVENTORY_HEIGHT = 166;
	public static final int GOLD_COLOUR = 0xF5C433;
	public static final int TEXT_POSITION_X = 54;
	public static final int TEXT_POSITION_Y = 8;
	
	public void render() {
		Minecraft mc = Minecraft.getMinecraft();
		// Don't show if it's the shop buy/sell GUI, for it renders current gold by itself:
		if (mc.currentScreen instanceof GuiShopBase) {
			return;
		}
		int x;
		int y;
		if (!(mc.currentScreen instanceof GuiShopBase) && mc.theWorld != null) {
			//TODO make this GUI placement configurable in the menu in order to not overlay other mods' GUIs.
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			x = scaledresolution.getScaledWidth() - GUI_GOLD_WIDTH;
			y = 0;
		} else {
			return;
		}
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(mc.thePlayer);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(x, y, 115, 0, GUI_GOLD_WIDTH, GUI_GOLD_HEIGHT);
		// Draw the number
		renderGoldText(stats.getGold(), x, y);
	}
	
	public void renderGoldText(int gold, int goldGuiX, int goldGuiY) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String text = String.valueOf(gold);
		int x = goldGuiX + TEXT_POSITION_X - fontRenderer.getStringWidth(text);
		int y = goldGuiY + TEXT_POSITION_Y;
		fontRenderer.drawString(text, x, y, GOLD_COLOUR);
	}
}
