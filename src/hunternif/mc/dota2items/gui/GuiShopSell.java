package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopSell;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiShopSell extends GuiShopBase {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	
	public GuiShopSell(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer.player, new ContainerShopSell(inventoryPlayer)); 
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.curTab = ShopTab.SELL;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		//RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Sell price", 78, 32, TITLE_COLOR);
		this.fontRenderer.drawString("Inventory", 8, 73, TITLE_COLOR);
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		ClientProxy.guiGold.renderGoldText(stats.getGold(), WIDTH - GuiGold.GUI_GOLD_WIDTH, 0);
		ItemStack stackOnSale = ((ContainerShopSell)this.inventorySlots).getTransactionSlot().getStack();
		int sellPrice = 0;
		if (stackOnSale != null && stackOnSale.getItem() instanceof Dota2Item) {
			sellPrice = ((Dota2Item)stackOnSale.getItem()).getSellPrice() * stackOnSale.stackSize;
		}
		renderSellPrice(sellPrice, 94, 49);
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	public void renderSellPrice(int price, int x, int y) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String text = String.valueOf(price);
		fontRenderer.drawString(text, x, y, PRICE_COLOR);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/shop_sell.png");
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
