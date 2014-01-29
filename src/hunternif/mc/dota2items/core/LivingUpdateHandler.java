package hunternif.mc.dota2items.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/**
 * If you need to update entities that have EntityStats assigned to them,
 * register an IEntityUpdater into this class instead of using an event handler.
 */
public class LivingUpdateHandler {
	private final StatsTracker statsTracker;
	private final List<IEntityUpdater> updaters = new ArrayList<IEntityUpdater>();
	
	public LivingUpdateHandler(StatsTracker statsTracker) {
		this.statsTracker = statsTracker;
	}
	
	public void registerEntityUpdater(IEntityUpdater updater) {
		if (!updaters.contains(updater)) {
			updaters.add(updater);
		}
	}
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		EntityStats stats = statsTracker.getEntityStats(event.entityLiving);
		if (stats == null) {
			return;
		}
		for (IEntityUpdater updater : updaters) {
			updater.update(event.entityLiving, stats, event);
		}
	}
}
