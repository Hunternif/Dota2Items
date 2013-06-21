package hunternif.mc.dota2items.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShopSell extends Container {
	private static final int PLAYER_INV_X = 8;
	private static final int PLAYER_INV_Y = 84;
	
	private InventoryBasic invTransaction;
	private InventoryPlayer invPlayer;
	private int transactionSlotNumber;
	
	public ContainerShopSell(InventoryPlayer inventoryPlayer) {
		this.invPlayer = inventoryPlayer;
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
		// The transaction slot
		invTransaction = new InventoryBasic("Selling", false, 1);
		transactionSlotNumber = addSlotToContainer(new Slot(invTransaction, 0, 20, 45)).slotNumber;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public Slot getTransactionSlot() {
		return (Slot)inventorySlots.get(transactionSlotNumber);
	}
	
	@Override
	public void onCraftGuiClosed(EntityPlayer player) {
		super.onCraftGuiClosed(player);
		if (!player.worldObj.isRemote) {
			ItemStack stack = invTransaction.getStackInSlotOnClosing(0);
			if (stack != null) {
				player.dropPlayerItem(stack);
			}
		}
	}
}
