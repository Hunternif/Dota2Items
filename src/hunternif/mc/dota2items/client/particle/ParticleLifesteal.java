package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleLifesteal extends Dota2Particle {

	public ParticleLifesteal(World world, double x, double y, double z, double distance) {
		super(world, x, y, z);
		this.motionY = 0.05 + 0.05*rand.nextFloat();
		this.particleScale = (float)(1d/(distance));
		setRandomMaxAge(6, 20);
		setTexturePositions(6, 0, 2, true);
		setGlowing();
		setFade(0.2f, 0.5f);
	}
	
	@Override
	public void onUpdate() {
		this.particleScale -= 0.2;
		super.onUpdate();
	}

}
