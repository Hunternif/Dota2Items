package hunternif.mc.dota2items.inventory;

import net.minecraft.inventory.Slot;

public class SlotColumnIcon extends Slot {

	public final ItemColumn column;
	
	public SlotColumnIcon(ItemColumn column, int x, int y) {
		super(null, 0, x, y);
		this.column = column;
	}

}
