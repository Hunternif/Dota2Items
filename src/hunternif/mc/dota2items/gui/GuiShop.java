package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.inventory.ContainerShop;

import java.util.Iterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

public class GuiShop extends GuiContainer {
	public static final int WIDTH = 212;
	public static final int HEIGHT = 245;
	private static final int TITLE_COLOR = 0x404040;
	
	public GuiShop(InventoryPlayer inventoryPlayer) {
		super(new ContainerShop(inventoryPlayer));
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
	}

	@Override
	public void initGui(){
		super.initGui();
		//TODO add button to buttonList
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Basics", 8, 6, TITLE_COLOR);
		this.fontRenderer.drawString("Upgrades", 80, 6, TITLE_COLOR);
		Iterator iterator = this.buttonList.iterator();
		while (iterator.hasNext()) {
			GuiButton guibutton = (GuiButton)iterator.next();
			if (guibutton.func_82252_a()) {
				guibutton.func_82251_b(par1 - this.guiLeft, par2 - this.guiTop);
				break;
			}
		}
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/shop.png");
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
