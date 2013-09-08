package hunternif.mc.dota2items.client.particle;

import net.minecraft.world.World;

public class ParticleLifesteal extends Dota2Particle {

	public ParticleLifesteal(World world, double x, double y, double z, double distance) {
		super(world, x, y, z, 0, 0, 0);
		this.particleScale = (float)(1d/(distance));
		this.particleMaxAge = (int)(6F / (this.rand.nextFloat() * 0.7F + 0.3F));
		this.particleTextureIndexX = 6;
		this.particleTextureIndexY = 0;
		this.motionX = 0;
		this.motionZ = 0;
		this.motionY = 0.05 + 0.05*rand.nextFloat();
	}
	
	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xf000f0;
	}
	
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.particleScale -= 0.2;
		float ageFraq = ((float)this.particleAge) / (float)this.particleMaxAge;
		if (ageFraq < 0.2f) {
			setAlphaF(ageFraq*5f);
		}
		if (ageFraq > 0.5f) {
			setAlphaF(1f - (ageFraq - 0.5f) * 2f);
		}
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

}
