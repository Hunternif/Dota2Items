package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.inventory.InventoryShop.Mode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShop extends Container {
	public static final float MAX_DISTANCE = 32;
	
	private static final int PLAYER_INV_X = 26;
	private static final int PLAYER_INV_Y = 163;
	private static final int SHOP_INV_X = 8;
	private static final int SHOP_INV_Y = 35;
	private static final int SELECTED_SLOT_X = 151;
	private static final int SELECTED_SLOT_Y = 135;
	private static final int RECIPE_RESULT_X = 15;
	private static final int RECIPE_Y = 135;
	private static final int RECIPE_INGRED_X = 57;
	
	private InventoryShop shopInventory = InventoryShop.regularShop();
	private InventoryRecipe recipeInv = new InventoryRecipe();
	private InventoryPlayer inventoryPlayer;
	
	public ContainerShop(InventoryPlayer inventoryPlayer) {
		this.inventoryPlayer = inventoryPlayer;
		// Assign player's inventory
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, PLAYER_INV_X + j * 18, PLAYER_INV_Y + i * 18));
			}
		}
		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryPlayer, i, PLAYER_INV_X + i * 18, 58 + PLAYER_INV_Y));
		}
		// Assign shopkeeper's inventory
		for (i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				addSlotToContainer(new SlotShop(shopInventory, i * 10 + j, SHOP_INV_X + j * 18, SHOP_INV_Y + i * 18));
			}
		}
		addSlotToContainer(new SlotShop(recipeInv, 0, RECIPE_RESULT_X, RECIPE_Y));
		for (i = 0; i < 4; i++) {
			addSlotToContainer(new SlotShop(recipeInv, i+1, RECIPE_INGRED_X + i*18, RECIPE_Y));
		}
		addSlotToContainer(new SlotShopTransaction(shopInventory, SELECTED_SLOT_X, SELECTED_SLOT_Y));
		//TODO implement search
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int mouseBtn, int keyPressed, EntityPlayer player) {
		if (keyPressed == 1) { // The ominous shift-click
			return null;
		}
		if (slotId >= 0) {
			Slot slot = (Slot)inventorySlots.get(slotId);
			if (slot instanceof SlotShop) {
				shopInventory.selectItemInSlot(slot.getSlotIndex());
				return null;
			} else if (slot instanceof SlotShopTransaction) {
				if (inventoryPlayer.getItemStack() != null && shopInventory.getMode() == Mode.BUYING) {
					//shopInventory.setInventorySlotContents(slot.getSlotIndex(), inventoryPlayer.getItemStack().copy());
					shopInventory.clearSelectedItem();
				}
			}
		}
		return super.slotClick(slotId, mouseBtn, keyPressed, player);
	}
	
	@Override
	public void onCraftGuiClosed(EntityPlayer player) {
		super.onCraftGuiClosed(player);
		if (!player.worldObj.isRemote && shopInventory.getMode() == Mode.SELLING) {
			ItemStack itemStack = shopInventory.getStackInSlotOnClosing(InventoryShop.TRANSACTION_SLOT);
			if (itemStack != null) {
				player.dropPlayerItem(itemStack);
			}
		}
	}
}
