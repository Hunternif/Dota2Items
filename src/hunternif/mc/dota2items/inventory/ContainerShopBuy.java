package hunternif.mc.dota2items.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerShopBuy extends Container {
	private static final int SHOP_INV_X = 8;
	private static final int SHOP_INV_Y = 60;
	
	public InventoryShop invShop = InventoryShop.newRegularShop();
	private int slotResultNumber;
	private int slotRecipeResultNumber;
	private int slotIngr1Number;
	private int slotIngr2Number;
	private int slotIngr3Number;
	private int slotIngr4Number;
	
	public ContainerShopBuy() {
		// Assign shopkeeper's inventory
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				addSlotToContainer(new SlotShop(invShop, i * 10 + j, SHOP_INV_X + j * 18, SHOP_INV_Y + i * 18));
			}
		}
		//TODO: Slots for the recipe: slots or buttons?
		slotRecipeResultNumber = addSlotToContainer(new SlotShop(invShop, InventoryShop.SLOT_RECIPE_RESULT, 51, 160)).slotNumber;
		slotIngr1Number = addSlotToContainer(new SlotShop(invShop, InventoryShop.SLOT_INGR_1, 19, 208)).slotNumber;
		slotIngr2Number = addSlotToContainer(new SlotShop(invShop, InventoryShop.SLOT_INGR_2, 19+(18+3)*1, 208)).slotNumber;
		slotIngr3Number = addSlotToContainer(new SlotShop(invShop, InventoryShop.SLOT_INGR_3, 19+(18+3)*2, 208)).slotNumber;
		slotIngr4Number = addSlotToContainer(new SlotShop(invShop, InventoryShop.SLOT_INGR_4, 19+(18+3)*3, 208)).slotNumber;
		// The result slot:
		slotResultNumber = addSlotToContainer(new SlotShopBuyResult(invShop, InventoryShop.SLOT_RESULT, 151, 203)).slotNumber;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public SlotShopBuyResult getSlotResult() {
		return (SlotShopBuyResult)inventorySlots.get(slotResultNumber);
	}
	
}
