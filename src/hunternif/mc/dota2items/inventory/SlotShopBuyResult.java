package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotShopBuyResult extends Slot {

	public SlotShopBuyResult(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		if (!(this.getStack().getItem() instanceof Dota2Item)) {
			return false;
		}
		Dota2Item item = (Dota2Item) this.getStack().getItem();
		return stats.getGold() >= item.getTotalPrice() * this.getStack().stackSize;
	}
}
