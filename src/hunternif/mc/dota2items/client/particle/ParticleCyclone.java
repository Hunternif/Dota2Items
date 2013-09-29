package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleCyclone extends Dota2Particle {
	public ParticleCyclone(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		setTexturePositions(5, 0);
		this.particleMaxAge = 20;
		this.particleScale = 2;
		setFade(0, 0.5f);
	}
	
	@Override
	protected float scaleAtAge(float ageFraq) {
		return ageFraq < 0.5f ? 2f : (2f + 2f * (ageFraq - 0.5f));
	}
}
