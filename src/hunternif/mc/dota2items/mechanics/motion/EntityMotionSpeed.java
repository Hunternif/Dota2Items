package hunternif.mc.dota2items.mechanics.motion;

import net.minecraft.entity.Entity;

public class EntityMotionSpeed extends Dota2EntityMotion {
	public float factor;
	
	public EntityMotionSpeed(Entity entity, float factor, long timeout) {
		super(entity, timeout);
		this.factor = factor;
	}

	@Override
	public void applyMotion() {
		if (entity.onGround) {
			entity.motionX *= factor;
			entity.motionZ *= factor;
		}
	}
}
