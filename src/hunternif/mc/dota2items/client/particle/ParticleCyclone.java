package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleCyclone extends Dota2Particle {
	//public static int invisibleAge = 1;
	public static int expandAge = 10;

	public ParticleCyclone(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		setTexturePositions(5, 0);
		this.particleMaxAge = 20;
		setFade(0, 0.5f);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
        /*if (particleAge == invisibleAge)
        	setAlphaF(0.7f);*/
        if (particleAge >= expandAge) {
        	particleScale += 0.2f;
        }
	}
}
