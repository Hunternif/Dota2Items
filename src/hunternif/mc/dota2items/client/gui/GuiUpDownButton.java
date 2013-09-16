package hunternif.mc.dota2items.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;

import org.lwjgl.opengl.GL11;

public class GuiUpDownButton extends GuiButton {
	protected static RenderItem itemRenderer = new RenderItem();
	
	public boolean down;
	private boolean isSelected;
	
	public GuiUpDownButton(int id, int x, int y, boolean down) {
		super(id, x, y, 11, 9, "");
		this.down = down;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.drawButton) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			// Render background:
			mc.renderEngine.func_110577_a(GuiShopBuy.texture);
			boolean isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int u = 230;
			int v = 95;
			if (!this.enabled) {
				v += this.height * 2;
			} else if (isSelected) {
				v += this.height * 1;
			} else if (isMouseOver) {
				v += this.height * 3;
			}
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean value) {
		isSelected = value;
	}
}
