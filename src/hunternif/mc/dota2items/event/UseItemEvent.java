package hunternif.mc.dota2items.event;

import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class UseItemEvent extends PlayerEvent {
	public Dota2Item item;

	public UseItemEvent(EntityPlayer player, Dota2Item item) {
		super(player);
		this.item = item;
	}
	
}
