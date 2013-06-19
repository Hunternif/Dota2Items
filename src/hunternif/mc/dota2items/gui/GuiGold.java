package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGold extends Gui {
	public static final int GUI_GOLD_WIDTH = 61;
	public static final int GUI_GOLD_HEIGHT = 23;
	public static final int GUI_INVENTORY_WIDTH = 176;
	public static final int GUI_INVENTORY_HEIGHT = 166;
	public static final int GOLD_COLOUR = 0xF5C433;
	public static final int TEXT_POSITION_X = 54;
	public static final int TEXT_POSITION_Y = 8;
	
	public void render() {
		Minecraft mc = Minecraft.getMinecraft();
		int x;
		int y;
		// Show gold when the inventory is open:
		if (mc.currentScreen instanceof GuiContainer &&
				!(mc.currentScreen instanceof GuiShop || mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiBeacon)) {
			x = (mc.currentScreen.width - GUI_INVENTORY_WIDTH)/2 + GUI_INVENTORY_WIDTH - GUI_GOLD_WIDTH;
			// If there are active potion effects, the inventory is shifted to the right.
			if (!mc.thePlayer.getActivePotionEffects().isEmpty()) {
				x += 60;
			}
			y = (mc.currentScreen.height - GUI_INVENTORY_HEIGHT)/2 - GUI_GOLD_HEIGHT;
		// Or in game:
		} else if (mc.currentScreen instanceof GuiShop) {
			x = (mc.currentScreen.width - GuiShop.WIDTH)/2 + GuiShop.WIDTH;
			y = (mc.currentScreen.height - GuiShop.HEIGHT)/2 + 130;
		} else if (mc.theWorld != null) {
			//TODO make this GUI placement configurable in the menu in order to not overlay other mods' GUIs.
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			x = scaledresolution.getScaledWidth() - GUI_GOLD_WIDTH;
			y = 0;
		} else {
			return;
		}
		EntityStats stats = Dota2Items.mechanics.getEntityStats(mc.thePlayer);
		if (stats != null) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/gold.png");
			drawTexturedModalRect(x, y, 0, 0, GUI_GOLD_WIDTH, GUI_GOLD_HEIGHT);
			// Draw the number
			FontRenderer fontRenderer = mc.fontRenderer;
			String text = String.valueOf(stats.getGold());
			x += TEXT_POSITION_X - fontRenderer.getStringWidth(text);
			y += TEXT_POSITION_Y;
			fontRenderer.drawString(text, x, y, GOLD_COLOUR);
		}
	}
}
