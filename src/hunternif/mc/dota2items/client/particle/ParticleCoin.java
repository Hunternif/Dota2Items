package hunternif.mc.dota2items.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ParticleCoin extends LoopingParticle {
	private Entity destination;
	/** Distance to destination on the previous tick. */
	private double lastDistance = Double.MAX_VALUE;
	
	/** Acceleration of coin towards the player, i.e. increment of velocity per tick. */
	private static final double accel = 0.06;
	/** gravity */
	private static final double gee = 0.04;
	
	public ParticleCoin(World world, double x, double y, double z, double velX,
			double velY, double velZ, Entity entity) {
		super(world, x, y, z, velX, velY, velZ);
		this.destination = entity;
		randomizeVelocity(0.04);
		particleMaxAge = MathHelper.ceiling_double_int(eta());
		
		setFPS(6 + rand.nextInt(12));
		setTexturePositions(0, 1, 5, true);
		setFade(0, 1);
		setLoopReversed();
	}
	
	@Override
	public void onUpdate() {
		// Gravity:
		if (!onGround) {
			motionY -= gee;
		}
		// Coins are attracted to player:
		Vec3 vector = getDestinationVector();
		vector = vector.normalize();
		motionX += vector.xCoord * accel;
		motionY += vector.yCoord * accel;
		motionZ += vector.zCoord * accel;
		super.onUpdate();
	}
	
	/** Estimated time of collision with the player. */
	private double eta() {
		Vec3 vector = getDestinationVector();
		double distance = vector.lengthVector();
		vector = vector.normalize();
		Vec3 velocity = worldObj.getWorldVec3Pool().getVecFromPool(motionX, motionY, motionZ);
		double velProj = vector.dotProduct(velocity);
		return (Math.sqrt(velProj*velProj  + 2*accel*distance) - velProj)/accel;
	}
	
	private Vec3 getDestinationVector() {
		return worldObj.getWorldVec3Pool().getVecFromPool(
				destination.posX - posX,
				destination.posY + destination.yOffset/2 - posY,
				destination.posZ - posZ);
	}
}
