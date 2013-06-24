package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleCyclone extends Dota2Particle {
	//public static int invisibleAge = 1;
	public static int expandAge = 10;

	public ParticleCyclone(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.particleTextureIndexX = 5;
		this.particleTextureIndexY = 0;
	}
	
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        particleAge++;
        /*if (particleAge == invisibleAge)
        	setAlphaF(0.7f);*/
        if (particleAge >= expandAge) {
        	particleScale += 0.2f;
        	if (particleAlpha > 0)
        		particleAlpha -= 0.05f;
        }
	}
}
