package hunternif.mc.dota2items.client.particle;

import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/** This particle's icon texture loops through its frames at a steady FPS rate. */
public class LoopingParticle extends Dota2Particle {

	private float fps = 12;
	private boolean loopIsReversing = false;
	
	public LoopingParticle(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	public LoopingParticle(World world, double x, double y, double z,
			double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
	}
	
	protected void setFPS(float fps) {
		this.fps = fps;
	}
	protected float getFPS() {
		return fps;
	}
	
	protected boolean isLoopReversing() {
		return loopIsReversing;
	}
	/** If true, frame sequence will be 1-2-...-(N-1)-N-(N-1)-...-2-1-... */
	protected void setLoopReversed() {
		loopIsReversing = true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (iconStages > 1) {
			int totalFrames = MathHelper.ceiling_float_int(((float)particleAge) * getFPS() / MCConstants.TICKS_PER_SECOND);
			int stage = totalFrames % iconStages;
			if (isLoopReversing()) {
				int doubledStage = totalFrames % (2*iconStages - 1);
				if (doubledStage > iconStages) {
					stage = 2*iconStages - doubledStage;
				}
			}
			setParticleTextureIndex(iconStartIndex + stage);
		}
	}
}
