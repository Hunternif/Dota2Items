package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.network.OpenGuiPacket;
import hunternif.mc.dota2items.util.MouseAction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.MathHelper;

public abstract class GuiShopBase extends GuiContainer {

	public static final int TAB_TITLE_COLOR_ACTIVE = 0x404040;
	public static final int TAB_TITLE_COLOR_INACTIVE = 0x000000;
	public static final int PRICE_COLOR = 0x000000;
	
	private static final int TAB_WIDTH = 42;
	private static final int TAB_HEIGHT = 23;
	private static final int TAB_KERN = 3;
	
	protected static enum ShopTab {
		BUY, SELL;
	}
	
	protected ShopTab curTab = ShopTab.BUY;
	protected EntityPlayer player;
	
	public GuiShopBase(EntityPlayer player, Container container) {
		super(container);
		this.player = player;
	}
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseAction) {
		if (mouseAction == MouseAction.UP) {
			int relMouseX = mouseX - this.guiLeft;
			int relMouseY = mouseY - this.guiTop;
			ShopTab newTab = curTab;
			if (relMouseY >= 0 && relMouseY <= TAB_HEIGHT) {
				if (relMouseX >= 0 && relMouseX <= TAB_WIDTH) {
					newTab = ShopTab.BUY;
				} else if (relMouseX >= TAB_WIDTH + TAB_KERN && relMouseX <= TAB_KERN + TAB_WIDTH*2) {
					newTab = ShopTab.SELL;
				}
			}
			if (newTab != curTab) {
				curTab = newTab;
				int guiId = -1;
				switch (curTab) {
				case BUY:
					guiId = GuiHandler.GUI_ID_SHOP_BUY;
					break;
				case SELL:
					guiId = GuiHandler.GUI_ID_SHOP_SELL;
					break;
				}
				int x = MathHelper.floor_double(player.posX);
				int y = MathHelper.floor_double(player.posY);
				int z = MathHelper.floor_double(player.posZ);
				
				OpenGuiPacket.sendOpenGuiPacket(guiId);
				return;
			}
		}
		super.mouseMovedOrUp(mouseX, mouseY, mouseAction);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Buy", 12, 9, curTab == ShopTab.BUY ? TAB_TITLE_COLOR_ACTIVE : TAB_TITLE_COLOR_INACTIVE);
		this.fontRenderer.drawString("Sell", TAB_WIDTH + TAB_KERN + 11, 9, curTab == ShopTab.SELL ? TAB_TITLE_COLOR_ACTIVE : TAB_TITLE_COLOR_INACTIVE);
		//RenderHelper.enableStandardItemLighting();
	}
	
	
}
