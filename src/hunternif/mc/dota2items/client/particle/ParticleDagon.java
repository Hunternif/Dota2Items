package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleDagon extends Dota2Particle {
	public ParticleDagon(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0, 0);
		this.particleScale = (this.rand.nextFloat() * 0.7F + 0.3F) * 2f;
		this.particleMaxAge = (int)(3F / (this.rand.nextFloat() * 0.7F + 0.3F));
		this.particleTextureIndexX = 8;
		this.particleTextureIndexY = 0;
		this.motionX = (rand.nextDouble()*2-1)*0.05;
		this.motionY = (rand.nextDouble()*2-1)*0.05;
		this.motionZ = (rand.nextDouble()*2-1)*0.05;
	}
	
	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xf000f0;
	}
	
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		float ageFraq = ((float)this.particleAge) / (float)this.particleMaxAge;
		if (ageFraq > 0.5f) {
			this.particleTextureIndexX = 9;
			setAlphaF(1f - (ageFraq - 0.5f) * 2f);
		}
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
}
