package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleDagon extends Dota2Particle {
	public ParticleDagon(World world, double x, double y, double z) {
		super(world, x, y, z);
		setRandomScale(0.6f, 2);
		setRandomMaxAge(3, 10);
		setFade(0, 0.5f);
		setTexturePositions(8, 0, 2, true);
		setGlowing();
		randomizeVelocity(0.05);
	}
}
