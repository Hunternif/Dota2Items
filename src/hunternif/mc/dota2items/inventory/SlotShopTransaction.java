package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.inventory.InventoryShop.Mode;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotShopTransaction extends Slot {
	private InventoryShop invShop;
	public SlotShopTransaction(InventoryShop invShop, int x, int y) {
		super(invShop, InventoryShop.TRANSACTION_SLOT, x, y);
		this.invShop = invShop;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer entityPlayer) {
		return invShop.getMode() == Mode.SELLING;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemStack.getItem() instanceof Dota2Item;
	}
}
