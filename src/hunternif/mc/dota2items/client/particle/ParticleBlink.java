package hunternif.mc.dota2items.client.particle;


import java.util.Random;

import net.minecraft.world.World;

public class ParticleBlink extends Dota2Particle {
	public static final int STAGES = 5;
	
	private int stage = 0;
	
	public ParticleBlink(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.rand = new Random((long)((x + y + z)*100000));
		stage = world.rand.nextInt(STAGES);
		this.particleScale = (this.rand.nextFloat() * 0.3F + 0.7F) * 2.0F;
		this.particleMaxAge = (int)(12.0F / (this.rand.nextFloat() * 0.5F + 0.5F));
		this.motionX = velX + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
		this.motionY = velY + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
		this.motionZ = velZ + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
		this.setAlphaF(1);
		this.particleTextureIndexX = stage;
		this.particleTextureIndexY = 0;
	}
	
	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xf000f0;
	}
	
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		float ageFraq = ((float)this.particleAge) / (float)this.particleMaxAge;
		if (ageFraq > 0.8f) {
			setAlphaF(1f - (ageFraq - 0.8f) * 5f);
		}
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}
		if (stage < STAGES-1 && this.rand.nextInt((stage+4)/2) == 1) {
			stage++;
			nextTextureIndexX();
		}

		this.motionY -= 0.03D * (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		
		// Random swaying motion
		this.motionX += 0.025*(rand.nextDouble() - 0.5);
		this.motionY += 0.025*(rand.nextDouble() - 0.5);
		this.motionZ += 0.025*(rand.nextDouble() - 0.5);
		
		// Slowing down
		this.motionX *= 0.8;
		this.motionY *= 0.8;
		this.motionZ *= 0.8;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}
}