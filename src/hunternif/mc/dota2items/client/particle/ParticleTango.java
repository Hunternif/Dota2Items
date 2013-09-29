package hunternif.mc.dota2items.client.particle;


import java.util.Random;

import net.minecraft.world.World;

public class ParticleTango extends Dota2Particle {
	public ParticleTango(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.rand = new Random((long)((x + y + z)*100000));
		setRandomScale(1, 2);
		setRandomMaxAge(12, 30);
		randomizeVelocity(0.02);
		setTexturePositions(10, 0, 3, true);
		setFade(0, 0.9f);
	}
	
	public void onUpdate() {
		// Random swaying motion
		if (this.ticksExisted % (4 + rand.nextInt(2)) == 0)
		randomizeVelocity(0.02);
		
		// Slowing down
		this.motionX *= 0.8;
		this.motionY *= 0.8;
		this.motionZ *= 0.8;
		
		// Gravity:
		this.motionY -= 0.02;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
		super.onUpdate();
	}
}