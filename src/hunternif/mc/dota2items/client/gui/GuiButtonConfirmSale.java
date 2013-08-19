package hunternif.mc.dota2items.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiButtonConfirmSale extends GuiButton {
	private static final ResourceLocation beaconTexture = new ResourceLocation("textures/gui/container/beacon.png");

	public GuiButtonConfirmSale(int id, int x, int y) {
		super(id, x, y, 22, 22, "");
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.drawButton) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			// Render background:
			mc.renderEngine.func_110577_a(beaconTexture);
			boolean isMouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int u = 0;
			int v = 219;
			if (!this.enabled) {
				u += this.width * 2;
			} else if (Mouse.isButtonDown(0)) {
				u += this.width * 1;
			} else if (isMouseOver) {
				u += this.width * 3;
			}
			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width, this.height);
			
			// Render the tick:
			this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.width*4 + 2, v + 2, 18, 18);
		}
	}
}
