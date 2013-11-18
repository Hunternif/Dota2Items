package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.Column;
import hunternif.mc.dota2items.inventory.ContainerShopShowcase;
import hunternif.mc.dota2items.inventory.InventoryShop;
import hunternif.mc.dota2items.inventory.SlotColumnIcon;
import hunternif.mc.dota2items.item.Dota2Item;

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
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiShopBuy extends GuiShopBase {
	protected static final ResourceLocation texture = new ResourceLocation(Dota2Items.ID+":textures/gui/container/shop_buy.png");
	
	public static final int WIDTH = 230;
	public static final int HEIGHT = 238;
	private static final int COLUMN_ICONS_X = 8;
	private static final int COLUMN_ICONS_Y = 41;
	private static final int COLOR_SEARCH = 0xffffff;
	public static final int COLUMNS = 11;
	public static final int SCROLLBAR_X = 210;
	public static final int SCROLLBAR_Y = 60;
	public static final int SCROLLBAR_WIDTH = 12;
	public static final int SCROLLBAR_HEIGHT = 70;
	public static final int SCROLL_ANCHOR_HEIGHT = 15;
	
	public GuiTextField filterField;
	/** True if the scrollbar is being dragged */
	private boolean isScrolling = false;
	/** True if the left mouse button was held down last time drawScreen was called. */
	private boolean wasClicking = false;
	/** Amount scrolled (0 = top, 1 = bottom) */
	private float curScroll = 0;
	private SlotColumnIcon[] columnIcons = new SlotColumnIcon[COLUMNS];
	
	private List<GuiRecipeButton> resultBtns = new ArrayList<GuiRecipeButton>();
	private List<GuiRecipeButton> ingredientBtns = new ArrayList<GuiRecipeButton>();
	private GuiUpDownButton upButton;
	
	public GuiShopBuy(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer.player, new ContainerShopShowcase(inventoryPlayer));
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.curTab = ShopTab.BUY;
		for (int i = 0; i < COLUMNS; i++) {
			columnIcons[i] = new SlotColumnIcon(Column.forId(i), COLUMN_ICONS_X+18*i+1, COLUMN_ICONS_Y+1);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		filterField = new GuiTextField(this.fontRenderer, this.guiLeft + 127, this.guiTop + 29, 96, this.fontRenderer.FONT_HEIGHT);
		filterField.setMaxStringLength(15);
		filterField.setEnableBackgroundDrawing(false);
		filterField.setFocused(false);
		filterField.setCanLoseFocus(true);
		filterField.setTextColor(COLOR_SEARCH);
		upButton = new GuiUpDownButton(1, guiLeft + 105, guiTop + 167, false);
		resetRecipeButtons();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public void drawScreen(int xMouse, int yMouse, float par3) {
		boolean mouseDown = Mouse.isButtonDown(0);
		if (!wasClicking && mouseDown && isPointInRegion(SCROLLBAR_X, SCROLLBAR_Y, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, xMouse, yMouse)) {
			isScrolling = needsScrollBar();
		}
		if (!mouseDown) {
			isScrolling = false;
		}
		wasClicking = mouseDown;
		if (isScrolling) {
			curScroll = ((float)yMouse - (float)SCROLLBAR_Y - 7.5f) / (float)(SCROLLBAR_HEIGHT - SCROLL_ANCHOR_HEIGHT);
			if (curScroll < 0) curScroll = 0;
			if (curScroll > 1) curScroll = 1;
			InventoryShop invShop = ((ContainerShopShowcase)inventorySlots).invShop;
			int scrollRow = MathHelper.floor_float(curScroll * (float)(invShop.getFilteredRows() - invShop.getRows()));
			if (invShop.getScrollRow() != scrollRow) {
				invShop.scrollToRow(scrollRow);
			}
		}
		super.drawScreen(xMouse, yMouse, par3);
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int wheelMove = Mouse.getEventDWheel();
		if (wheelMove != 0 && needsScrollBar()) {
			wheelMove = wheelMove > 0 ? -1 : 1;
			InventoryShop invShop = ((ContainerShopShowcase)inventorySlots).invShop;
			int scrollRow = invShop.getScrollRow() + wheelMove;
			if (scrollRow < 0) scrollRow = 0;
			if (scrollRow > invShop.getFilteredRows() - invShop.getRows()) scrollRow = invShop.getFilteredRows() - invShop.getRows();
			curScroll = (float) scrollRow / (float)(invShop.getFilteredRows() - invShop.getRows());
			invShop.scrollToRow(scrollRow);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//RenderHelper.disableStandardItemLighting();
		this.fontRenderer.drawString("Shopkeeper", 8, 29, TITLE_COLOR);
		this.fontRenderer.drawString("Price", 128, 139, TITLE_COLOR);
		this.fontRenderer.drawString("Purchase", 128, 160, TITLE_COLOR);
		this.fontRenderer.drawString("Inventory", 128, 204, TITLE_COLOR);
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		GuiGold.renderGoldText(stats.getGold(), WIDTH - GuiGold.GUI_GOLD_WIDTH, 0);
		ItemStack resultStack = ((ContainerShopShowcase)this.inventorySlots).getSlotResult().getStack();
		int price = Dota2Item.getPrice(resultStack);
		renderBuyPrice(price, 187, 139, stats.getGold() >= price);
		
		// Column icons' hovering text
		for (int i = 0; i < COLUMNS; i++) {
			if (isMouseOverSlot(columnIcons[i], mouseX, mouseY)) {
				List<String> hoverStrings = Arrays.asList(columnIcons[i].column.getColoredName());
				// Draw hovering string using this.fontRenderer:
				this.func_102021_a(hoverStrings, mouseX - guiLeft, mouseY - guiTop);
			}
		}
		
		// Selected item's recipe
		for (Object btnObj : buttonList) {
			if (btnObj instanceof GuiRecipeButton) {
				GuiRecipeButton btn = (GuiRecipeButton) btnObj;
				if (btn.itemStack == null) {
					continue;
				}
				// It is always permitted to click on the button, for example to view the recipe
				// But it is displayed as enabled only if you have enough money:
				btn.displayEnabled = shopContains(btn.itemStack.getItem()) && Dota2Item.canBuy(btn.itemStack, player);
				
				btn.selected = resultStack != null && btn.itemStack.itemID == resultStack.itemID;
				
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
		// Draw GUI background
		mc.renderEngine.bindTexture(texture);
		int x = this.guiLeft;
		int y = this.guiTop;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		// Draw scrollbar
		x += 210;
		y += 60 + MathHelper.floor_float((float)(SCROLLBAR_HEIGHT - SCROLL_ANCHOR_HEIGHT) * curScroll);
		drawTexturedModalRect(x, y, 232 + (needsScrollBar() ? 0 : 12), 0, 12, 15);
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
			boolean canBuy = Dota2Item.canBuy(btn.itemStack, player);
			if (canBuy) {
				((ContainerShopShowcase)this.inventorySlots).setResultItem(btn.itemStack);
			}
			if (Dota2Item.hasRecipe(btn.itemStack) && (!canBuy || btn.selected)) {
				// When clicking on an item that has recipe, first set it as result;
				// On the next click when the item is already selected, or if the item
				// can't be set as result (can't buy), will the recipe be shown.
				((ContainerShopShowcase)this.inventorySlots).setRecipeResult((Dota2Item)btn.itemStack.getItem());
			} else if (Dota2Item.isUsedInRecipe(btn.itemStack) && (!canBuy || btn.selected)) {
				// And on the next click, or if can't buy, recipe hierarchy will be shown,
				// i.e. what items use this item in their recipe.
				((ContainerShopShowcase)this.inventorySlots).setRecipeIngredient((Dota2Item)btn.itemStack.getItem());
			}
		} else if (button == upButton) {
			List<ItemStack> results = ((ContainerShopShowcase)this.inventorySlots).getRecipeResults();
			((ContainerShopShowcase)this.inventorySlots).setRecipeIngredient((Dota2Item)results.get(0).getItem());
		}
	}
	
	private void updateFilter() {
		((ContainerShopShowcase)this.inventorySlots).invShop.setFilterStr(filterField.getText());
		curScroll = 0;
		((ContainerShopShowcase)this.inventorySlots).invShop.scrollToRow(0);
		//PacketDispatcher.sendPacketToServer(new ShopBuySetFilterPacket(filterField.getText()).makePacket());
	}
	
	private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		return this.isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY);
	}
	
	private void resetRecipeButtons() {
		buttonList.clear();
		buttonList.add(upButton);
		upButton.enabled = false;
		int id = -1;
		List<ItemStack> results = ((ContainerShopShowcase)this.inventorySlots).getRecipeResults();
		if (results.size() == 1 && !((Dota2Item) results.get(0).getItem()).getUsedInRecipes().isEmpty()) {
			upButton.enabled = true;
		}
		if (!results.isEmpty()) {
			resultBtns.clear();
			int x = guiLeft + 66 - MathHelper.floor_float( ((float)results.size())/2f * (18+3) );
			int y = guiTop + 142;
			for (int i = 0; i < results.size(); i++) {
				GuiRecipeButton btn = new GuiRecipeButton(id, x, y, results.get(i));
				buttonList.add(btn);
				resultBtns.add(btn);
				id --;
				x += 18+3;
			}
		}
		List<ItemStack> ingredients = ((ContainerShopShowcase)this.inventorySlots).getRecipeIngredients();
		if (!ingredients.isEmpty()) {
			ingredientBtns.clear();
			int x = guiLeft + 66 - MathHelper.floor_float( ((float)ingredients.size())/2f * (18+3) );
			int y = guiTop + 186;
			for (int i = 0; i < ingredients.size(); i++) {
				GuiRecipeButton btn = new GuiRecipeButton(id, x, y, ingredients.get(i));
				buttonList.add(btn);
				ingredientBtns.add(btn);
				id --;
				x += 18+3;
			}
		}
	}
	
	/** Only reset buttons, when their contents change. Otherwise they flick weirdly. */
	private boolean shouldResetRecipeButtons() {
		if (resultBtns.isEmpty()) {
			return true;
		}
		// Return true if result buttons have changed:
		List<ItemStack> recipeResults = ((ContainerShopShowcase)this.inventorySlots).getRecipeResults();
		if (recipeResults.size() != resultBtns.size()) {
			return true;
		}
		for (int i = 0; i < recipeResults.size(); i++) {
			if (recipeResults.get(i).itemID != resultBtns.get(i).itemStack.itemID) {
				return true;
			}
		}
		// Return true if ingredient buttons have changed:
		List<ItemStack> recipeIngredients = ((ContainerShopShowcase)this.inventorySlots).getRecipeIngredients();
		if (recipeIngredients.size() != ingredientBtns.size()) {
			return true;
		}
		for (int i = 0; i < recipeIngredients.size(); i++) {
			if (recipeIngredients.get(i).itemID != ingredientBtns.get(i).itemStack.itemID) {
				return true;
			}
		}
		return false;
	}
	
	private boolean shopContains(Item item) {
		if (item.itemID == Config.recipe.getID()) {
			return true;
		} else if (item instanceof Dota2Item) {
			 return ((ContainerShopShowcase)this.inventorySlots).invShop.contains((Dota2Item)item);
		} else {
			return false;
		}
	}
	
	private boolean needsScrollBar() {
		return ((ContainerShopShowcase)this.inventorySlots).invShop.getFilteredRows() >
				((ContainerShopShowcase)this.inventorySlots).invShop.getRows();
	}
}
