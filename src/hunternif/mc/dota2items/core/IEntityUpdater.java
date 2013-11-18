package hunternif.mc.dota2items.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/**
 * Implementations of this interface process EntityStats every server tick,
 * which is more efficient than looking up EntityStats on LivingUpdateEvent.
 */
public interface IEntityUpdater {
	void update(EntityLivingBase entity, EntityStats stats, LivingUpdateEvent event);
}
