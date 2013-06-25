package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotShopBuyResult extends Slot {

	public SlotShopBuyResult(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		return stats.getGold() >= Dota2Item.getPrice(getStack());
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		stats.removeGold( Dota2Item.getPrice(stack) );
		super.onPickupFromSlot(player, stack);
	}
}
