package hunternif.mc.dota2items.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.Event;

public class EntityUnimmobilizeEvent extends Event {
	public final Entity entity;
	
	public EntityUnimmobilizeEvent(Entity entity) {
		super();
		this.entity = entity;
	}
}
