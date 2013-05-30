package hunternif.mc.dota2items.mechanics.motion;

import hunternif.mc.dota2items.event.EntityImmobilizeEvent;
import hunternif.mc.dota2items.event.EntityUnimmobilizeEvent;
import hunternif.mc.dota2items.mechanics.IUpdatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.ForgeSubscribe;

public class Dota2EntityMotionHandler implements IUpdatable {
	public static long defaultImmobilizeDuration = 3 * 60 * 20; // 5 minutes
	
	private List<Dota2EntityMotion> clientEntities = Collections.synchronizedList(new ArrayList<Dota2EntityMotion>());
	private List<Dota2EntityMotion> serverEntities = Collections.synchronizedList(new ArrayList<Dota2EntityMotion>());
	
	public void immobilize(Entity entity, long timeout) {
		if (entity == null) return;
		if (entity.worldObj.isRemote) {
			clientEntities.add(new EntityMotionDisabled(entity, entity.posX, entity.posY, entity.posZ, timeout));
		} else {
			serverEntities.add(new EntityMotionDisabled(entity, entity.posX, entity.posY, entity.posZ, timeout));
		}
	}
	
	public void unimmobilize(Entity entity) {
		if (entity == null) return;
		if (entity.worldObj.isRemote) {
			synchronized (clientEntities) {
				Iterator<Dota2EntityMotion> iter = clientEntities.iterator();
				while (iter.hasNext()) {
					Dota2EntityMotion motion = iter.next();
					if (motion instanceof EntityMotionDisabled && entity.isEntityEqual(motion.entity)) {
						iter.remove();
						return;
					}
				}
			}
		} else {
			synchronized (serverEntities) {
				Iterator<Dota2EntityMotion> iter = serverEntities.iterator();
				while (iter.hasNext()) {
					Dota2EntityMotion motion = iter.next();
					if (motion instanceof EntityMotionDisabled && entity.isEntityEqual(motion.entity)) {
						iter.remove();
						return;
					}
				}
			}
		}
	}
	
	private void updateClientEntities() {
		synchronized (clientEntities) {
			Iterator<Dota2EntityMotion> iter = clientEntities.iterator();
			while (iter.hasNext()) {
				Dota2EntityMotion motion = iter.next();
				if (motion.entity.worldObj.getTotalWorldTime() >= motion.endTime) {
					iter.remove();
					continue;
				}
				motion.applyMotion();
			}
		}
	}
	
	private void updateServerEntities() {
		synchronized (serverEntities) {
			Iterator<Dota2EntityMotion> iter = serverEntities.iterator();
			while (iter.hasNext()) {
				Dota2EntityMotion motion = iter.next();
				if (motion.entity.worldObj.getTotalWorldTime() >= motion.endTime) {
					iter.remove();
					continue;
				}
				motion.applyMotion();
			}
		}
	}
	
	public void onUpdate(boolean isRemote) {
		if (isRemote) updateClientEntities();
		else updateServerEntities();
	}
	
	@ForgeSubscribe
	public void onImmobilizeEvent(EntityImmobilizeEvent event) {
		immobilize(event.entity, defaultImmobilizeDuration);
	}
	
	@ForgeSubscribe
	public void onUnimmobilizeEvent(EntityUnimmobilizeEvent event) {
		unimmobilize(event.entity);
	}
}
