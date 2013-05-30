package hunternif.mc.dota2items.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.Event;

public class EntityImmobilizeEvent extends Event {
	public final Entity entity;
	
	public EntityImmobilizeEvent(Entity entity) {
		super();
		this.entity = entity;
	}
}
