package hunternif.mc.dota2items.mechanics.motion;

import net.minecraft.entity.Entity;

public class EntityMotionDisabled extends Dota2EntityMotion {
	public double x;
	public double y;
	public double z;
	
	public EntityMotionDisabled(Entity entity, double x, double y, double z, long timeout) {
		super(entity, timeout);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void applyMotion() {
		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		entity.posX = x;
		entity.posY = y;
		entity.posZ = z;
		entity.fallDistance = 0;
	}

}
