package hunternif.mc.dota2items.client.particle;


import java.util.Random;

import net.minecraft.world.World;

public class ParticleBlink extends Dota2Particle {
	public ParticleBlink(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.rand = new Random((long)((x + y + z)*100000));
		setRandomScale(1.4f, 2f);
		setRandomMaxAge(12, 24);
		randomizeVelocity(0.04);
		setTexturePositions(0, 0, 5, true);
		setGlowing();
		setFade(0, 0.8f);
	}
	
	public void onUpdate() {
		// Random swaying motion
		randomizeVelocity(0.0125);
		
		// Slowing down
		this.motionX *= 0.8;
		this.motionY *= 0.8;
		this.motionZ *= 0.8;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
		super.onUpdate();
	}
}