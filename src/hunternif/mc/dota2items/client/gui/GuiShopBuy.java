package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import hunternif.mc.dota2items.inventory.ItemColumn;
import hunternif.mc.dota2items.inventory.SlotColumnIcon;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.network.ShopFilterInputPacket;
import hunternif.mc.dota2items.network.ShopSetResultPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiShopBuy extends GuiShopBase {
	public static final int WIDTH = 230;
	public static final int HEIGHT = 238;
	private static final int COLUMN_ICONS_X = 8;
	private static final int COLUMN_ICONS_Y = 41;
	private static final int COLOR_SEARCH = 0xffffff;
	public static final int FILTER_STR_LENGTH = 15;
	public static final int COLUMNS = 11;
	
	public GuiTextField filterField;
	private SlotColumnIcon[] columnIcons = new SlotColumnIcon[COLUMNS];
	
	private GuiRecipeButton recipeResultButton;
	private List<GuiRecipeButton> recipeButtons = new ArrayList<GuiRecipeButton>();
	
	public GuiShopBuy(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer.player, new ContainerShopBuy(inventoryPlayer));
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.curTab = ShopTab.BUY;
		for (int i = 0; i < COLUMNS; i++) {
			columnIcons[i] = new SlotColumnIcon(ItemColumn.forId(i), COLUMN_ICONS_X+18*i+1, COLUMN_ICONS_Y+1);
		}
		//TODO make buttons to traverse full recipe hierarchy up and down.
		//TODO implement scrolling.
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		filterField = new GuiTextField(this.fontRenderer, this.guiLeft + 127, this.guiTop + 29, 96, this.fontRenderer.FONT_HEIGHT);
		filterField.setMaxStringLength(FILTER_STR_LENGTH);
		filterField.setEnableBackgroundDrawing(false);
		filterField.setFocused(false);
		filterField.setCanLoseFocus(true);
		filterField.setTextColor(COLOR_SEARCH);
		resetRecipeButtons();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Shopkeeper", 8, 29, TITLE_COLOR);
		this.fontRenderer.drawString("Price", 114, 139, TITLE_COLOR);
		this.fontRenderer.drawString("Purchase", 114, 160, TITLE_COLOR);
		this.fontRenderer.drawString("Inventory", 114, 204, TITLE_COLOR);
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		ClientProxy.guiGold.renderGoldText(stats.getGold(), WIDTH - GuiGold.GUI_GOLD_WIDTH, 0);
		ItemStack resultStack = ((ContainerShopBuy)this.inventorySlots).getSlotResult().getStack();
		int price = Dota2Item.getPrice(resultStack);
		renderBuyPrice(price, 180, 139, stats.getGold() >= price);
		
		// Column icons' hovering text
		for (int i = 0; i < COLUMNS; i++) {
			if (isMouseOverSlot(columnIcons[i], mouseX, mouseY)) {
				List<String> hoverStrings = Arrays.asList(columnIcons[i].column.name);
				drawHoveringText(hoverStrings, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
			}
		}
		
		// Selected item's recipe
		for (Object btnObj : buttonList) {
			if (btnObj instanceof GuiRecipeButton) {
				GuiRecipeButton btn = (GuiRecipeButton) btnObj;
				if (btn.itemStack == null) {
					continue;
				}
				// Permit clicking on the button if this item can be bought now
				// or if it has a recipe to view.
				btn.enabled = shopContains(btn.itemStack.getItem()) && Dota2Item.canBuy(btn.itemStack, player) ||
						btn != recipeResultButton && Dota2Item.hasRecipe(btn.itemStack);
				
				if (isPointInRegion(btn.xPosition - guiLeft, btn.yPosition - guiTop, 18, 18, mouseX, mouseY)) {
					drawItemStackTooltip(btn.itemStack, mouseX - guiLeft, mouseY - guiTop);
				}
			}
		}
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/gui/shop_buy.png");
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		filterField.drawTextBox();
	}
	
	private void renderBuyPrice(int price, int x, int y, boolean canAfford) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String text = (canAfford ? "" : EnumChatFormatting.DARK_RED) + String.valueOf(price);
		fontRenderer.drawString(text, x, y, PRICE_COLOR);
	}
	
	@Override
	protected void keyTyped(char ch, int key) {
		if (filterField.isFocused()) {
			if (filterField.textboxKeyTyped(ch, key)) {
				updateFilter();
			}
		} else {
			super.keyTyped(ch, key);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		filterField.mouseClicked(x, y, button);
		if (shouldResetRecipeButtons()) {
			resetRecipeButtons();
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button instanceof GuiRecipeButton) {
			GuiRecipeButton btn = (GuiRecipeButton) button;
			if (btn.itemStack == null) {
				return;
			}
			boolean shouldResetButtons = false;
			if (Dota2Item.canBuy(btn.itemStack, player)) {
				((ContainerShopBuy)this.inventorySlots).setResultItem(btn.itemStack);
				ShopSetResultPacket.sendToServer(btn.itemStack);
				shouldResetButtons = true;
			}
			if (Dota2Item.hasRecipe(btn.itemStack)) {
				((ContainerShopBuy)this.inventorySlots).setRecipeResultItem((Dota2Item)btn.itemStack.getItem());
				shouldResetButtons = true;
			}
			if (shouldResetButtons) {
				resetRecipeButtons();
			}
		}
	}
	
	private void updateFilter() {
		((ContainerShopBuy)this.inventorySlots).invShop.setFilterStr(filterField.getText());
		ShopFilterInputPacket.sendToServer(filterField.getText());
	}
	
	private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		return this.isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY);
	}
	
	private void resetRecipeButtons() {
		buttonList.clear();
		ItemStack resultStack = ((ContainerShopBuy)this.inventorySlots).getResultItem();
		ItemStack recipeResultStack = ((ContainerShopBuy)this.inventorySlots).getRecipeResultItem();
		if (recipeResultStack != null) {
			recipeResultButton = new GuiRecipeButton(-1, guiLeft + 51, guiTop + 142, recipeResultStack);
			recipeResultButton.enabled = false;
			if (resultStack != null && recipeResultStack.itemID == resultStack.itemID) {
				recipeResultButton.setSelected(true);
			}
			buttonList.add(recipeResultButton);
		}
		List<ItemStack> ingredients = ((ContainerShopBuy)this.inventorySlots).getRecipeIngredients();
		if (ingredients != null && !ingredients.isEmpty()) {
			recipeButtons.clear();
			int id = -2;
			int x = guiLeft + 60 - MathHelper.floor_float( ((float)ingredients.size())/2f * (18+3) );
			int y = guiTop + 186;
			for (int i = 0; i < ingredients.size(); i++) {
				GuiRecipeButton btn = new GuiRecipeButton(id, x, y, ingredients.get(i));
				btn.enabled = false;
				if (resultStack != null && ingredients.get(i).itemID == resultStack.itemID) {
					btn.setSelected(true);
				}
				buttonList.add(btn);
				recipeButtons.add(btn);
				id -= i;
				x += 18+3;
			}
		}
	}
	
	private boolean shouldResetRecipeButtons() {
		if (recipeResultButton == null || recipeResultButton.itemStack == null) {
			return true;
		}
		ItemStack recipeResult = ((ContainerShopBuy)this.inventorySlots).getRecipeResultItem();
		if (recipeResult != null && recipeResult.itemID != recipeResultButton.itemStack.itemID) {
			return true;
		}
		return false;
	}
	
	private boolean shopContains(Item item) {
		if (item.itemID == Config.recipe.getID()) {
			return true;
		} else if (item instanceof Dota2Item) {
			 return ((ContainerShopBuy)this.inventorySlots).invShop.contains((Dota2Item)item);
		} else {
			return false;
		}
	}
}
