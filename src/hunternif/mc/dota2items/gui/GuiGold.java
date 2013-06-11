package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;

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
		// Only show when the player inventory is open
		if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
			EntityStats stats = Dota2Items.mechanics.getEntityStats(Minecraft.getMinecraft().thePlayer);
			if (stats != null) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				Minecraft.getMinecraft().renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/gold.png");
				int x = (Minecraft.getMinecraft().currentScreen.width - GUI_INVENTORY_WIDTH)/2 + GUI_INVENTORY_WIDTH - GUI_GOLD_WIDTH;
				// If there are active potion effects, the inventory is shifted to the right.
				if (!Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty()) {
					x += 60;
				}
				int y = (Minecraft.getMinecraft().currentScreen.height - GUI_INVENTORY_HEIGHT)/2 - GUI_GOLD_HEIGHT;
				drawTexturedModalRect(x, y, 0, 0, GUI_GOLD_WIDTH, GUI_GOLD_HEIGHT);
				// Draw the number
				FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
				String text = String.valueOf(stats.getGold());
				x += TEXT_POSITION_X - fontRenderer.getStringWidth(text);
				y += TEXT_POSITION_Y;
				fontRenderer.drawString(text, x, y, GOLD_COLOUR);
			}
		}
	}
}
