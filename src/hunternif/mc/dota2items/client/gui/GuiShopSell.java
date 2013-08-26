package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopSell;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.network.ShopSellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiShopSell extends GuiShopBase {
	protected static final ResourceLocation texture = new ResourceLocation(Dota2Items.ID+":textures/gui/container/shop_sell.png");
	
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;
	
	private GuiButtonConfirmSale okBtn;
	
	public GuiShopSell(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer.player, new ContainerShopSell(inventoryPlayer)); 
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.curTab = ShopTab.SELL;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		okBtn = new GuiButtonConfirmSale(-1, guiLeft + 139, guiTop + 43);
		okBtn.enabled = false;
		buttonList.add(okBtn);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == okBtn) {
			ItemStack stackOnSale = ((ContainerShopSell)this.inventorySlots).getSellingSlot().getStack();
			int sellPrice = Dota2Item.getSellPrice(stackOnSale);
			((ContainerShopSell)this.inventorySlots).putStackInSlot(0, null);
			EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
			stats.addGold(sellPrice);
			PacketDispatcher.sendPacketToServer(new ShopSellPacket().makePacket());
			Minecraft.getMinecraft().sndManager.playSoundFX(Sound.COINS.getName(), 1, 1);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		//RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Sell price", 78, 32, TITLE_COLOR);
		this.fontRenderer.drawString("Inventory", 8, 73, TITLE_COLOR);
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		ClientProxy.guiGold.renderGoldText(stats.getGold(), WIDTH - GuiGold.GUI_GOLD_WIDTH, 0);
		ItemStack stackOnSale = ((ContainerShopSell)this.inventorySlots).getSellingSlot().getStack();
		int sellPrice = Dota2Item.getSellPrice(stackOnSale);
		okBtn.enabled = sellPrice > 0;
		renderSellPrice(sellPrice, 94, 49);
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	private void renderSellPrice(int price, int x, int y) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String text = String.valueOf(price);
		fontRenderer.drawString(text, x, y, PRICE_COLOR);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.func_110577_a(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
