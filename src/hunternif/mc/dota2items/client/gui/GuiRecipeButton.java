package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiRecipeButton extends GuiButton {
	protected static RenderItem itemRenderer = new RenderItem();
	
	public ItemStack itemStack;
	private boolean isSelected;
	
	public GuiRecipeButton(int id, int x, int y, ItemStack itemStack) {
		super(id, x, y, 18, 18, "");
		this.itemStack = itemStack;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.drawButton) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			// Render background:
			mc.renderEngine.bindTexture("/mods/" + Dota2Items.ID + "/textures/gui/shop_buy.png");
			boolean isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int u = 230;
			int v = 23;
			if (!this.enabled) {
				v += this.width * 2;
			} else if (isSelected) {
				v += this.width * 1;
			} else if (isMouseOver) {
				v += this.width * 3;
			}
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
			
			// Render item on top of the button:
			if (itemStack != null) {
				FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
				if (font == null) font = mc.fontRenderer;
				itemRenderer.renderItemAndEffectIntoGUI(font, mc.renderEngine, itemStack, this.xPosition + 1, this.yPosition + 1);
				itemRenderer.renderItemOverlayIntoGUI(font, mc.renderEngine, itemStack, this.xPosition + 1, this.yPosition + 1, (String)null);
			}
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean value) {
		isSelected = value;
	}
}
