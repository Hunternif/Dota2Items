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
	
	private InventoryBasic invSelling;
	private InventoryPlayer invPlayer;
	
	public ContainerShopSell(InventoryPlayer inventoryPlayer) {
		this.invPlayer = inventoryPlayer;
		// Assign trade slots:
		invSelling = new InventoryBasic("Selling", false, 1);
		addSlotToContainer(new Slot(invSelling, 0, 20, 45));
		// Assign player's inventory:
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, PLAYER_INV_X + j * 18, PLAYER_INV_Y + i * 18));
			}
		}
		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryPlayer, i, PLAYER_INV_X + i * 18, 58 + PLAYER_INV_Y));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public Slot getSellingSlot() {
		return (Slot)inventorySlots.get(0);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!player.worldObj.isRemote) {
			ItemStack stack = invSelling.getStackInSlotOnClosing(0);
			if (stack != null) {
				player.dropPlayerItem(stack);
			}
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotID);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID != 0) {
				if (slotID >= 1 && slotID < 28) {
					if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
						return null;
					}
				} else if (slotID >= 28 && slotID < 37 && !this.mergeItemStack(itemstack1, 1, 28, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}
}
