package hunternif.mc.dota2items.mechanics.motion;

import net.minecraft.entity.Entity;

public abstract class Dota2EntityMotion {
	public Entity entity;
	public long endTime;
	
	public Dota2EntityMotion(Entity entity, long timeout) {
		this.entity = entity;
		this.endTime = entity.worldObj.getTotalWorldTime() + timeout;
	}
	
	public abstract void applyMotion();
}
