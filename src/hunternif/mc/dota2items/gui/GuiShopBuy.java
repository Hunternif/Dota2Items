package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

public class GuiShopBuy extends GuiShopBase {
	public static final int WIDTH = 212;
	public static final int HEIGHT = 239;
	
	public GuiShopBuy(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer.player, new ContainerShopBuy());
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.curTab = ShopTab.BUY;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		//RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Shopkeeper", 8, 29, TITLE_COLOR);
		this.fontRenderer.drawString("Price", 114, 153, TITLE_COLOR);
		this.fontRenderer.drawString("Purchase", 114, 186, TITLE_COLOR);
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		ClientProxy.guiGold.renderGoldText(stats.getGold(), WIDTH - GuiGold.GUI_GOLD_WIDTH, 0);
		ItemStack resultStack = ((ContainerShopBuy)this.inventorySlots).getSlotResult().getStack();
		int price = 0;
		if (resultStack != null && resultStack.getItem() instanceof Dota2Item) {
			price = ((Dota2Item)resultStack.getItem()).getTotalPrice() * resultStack.stackSize;
		}
		renderBuyPrice(price, 170, 166, stats.getGold() >= price);
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/shop_buy.png");
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	public void renderBuyPrice(int price, int x, int y, boolean canAfford) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String text = (canAfford ? "" : EnumChatFormatting.DARK_RED) + String.valueOf(price);
		fontRenderer.drawString(text, x, y, PRICE_COLOR);
	}
}
