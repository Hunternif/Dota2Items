package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleArcaneBoots extends ParticleGlow {
	public ParticleArcaneBoots(World world, double x, double y, double z,
			double velX, double velY, double velZ, float distance) {
		super(world, x, y, z, velX, velY, velZ);
		this.particleScale = 0.7f + (2f*distance - distance*distance);
		this.particleMaxAge = 15;
		setFade(0.3f, 0.6f);
		setBaseAlpha(distance/2f);
		setColor(0x55aefe);
	}
}
