package hunternif.mc.dota2items.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;

public class CooldownEndDisplayEvent extends Event {
	
	public final Integer slotId;
	public final ItemStack itemStack;
	
	public CooldownEndDisplayEvent(int slotId, ItemStack itemStack) {
		super();
		this.slotId = new Integer(slotId);
		this.itemStack = itemStack;
	}
}
