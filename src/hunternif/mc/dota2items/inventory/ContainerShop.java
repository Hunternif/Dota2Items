package hunternif.mc.dota2items.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerShop extends Container {
	public static final float MAX_DISTANCE = 32;
	
	private static final int PLAYER_INV_X = 26;
	private static final int PLAYER_INV_Y = 163;
	private static final int SHOP_INV_X = 8;
	private static final int SHOP_INV_Y = 35;
	
	private InventoryShop shopInventory = InventoryShop.regularShop();
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
		//TODO implement search
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
