package hunternif.mc.dota2items.client.particle;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleMiss extends EntityFX {
	protected static final ResourceLocation texture = new ResourceLocation(Dota2Items.ID+":textures/miss.png");
	private static final float width = 21f/16f;
	private static final float height = 9f/16f;

	public ParticleMiss(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0, 0);
		this.particleScale = 2f;
		this.particleMaxAge = 20;
		this.motionX = 0;
		this.motionZ = 0;
		this.motionY = 0.05;
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
	
	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		// Apparently this defeats the purpose of Tessellator, but it's the only
		// way I found to render custom particle texture without interfering with
		// vanilla particles.
		//NOTE think of a way to optimize custom particle rendering
		if (tessellator.isDrawing) {
			tessellator.draw();
		}
		Minecraft.getMinecraft().renderEngine.func_110577_a(texture);
		tessellator.startDrawingQuads();
		tessellator.setBrightness(getBrightnessForRender(partialTick));
		
		float f6 = 0;
		float f7 = 1;
		float f8 = 0;
		float f9 = 1;
		float f10w = 0.1F * this.particleScale * width;
		float f10h = 0.1f * this.particleScale * height;

		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);
		float f14 = 1.0F;
		tessellator.setColorRGBA_F(this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha);
		tessellator.addVertexWithUV((double)(f11 - rotX * f10w - rotYZ * f10h), (double)(f12 - rotXZ * f10h), (double)(f13 - rotZ * f10w - rotXY * f10h), (double)f7, (double)f9);
		tessellator.addVertexWithUV((double)(f11 - rotX * f10w + rotYZ * f10h), (double)(f12 + rotXZ * f10h), (double)(f13 - rotZ * f10w + rotXY * f10h), (double)f7, (double)f8);
		tessellator.addVertexWithUV((double)(f11 + rotX * f10w + rotYZ * f10h), (double)(f12 + rotXZ * f10h), (double)(f13 + rotZ * f10w + rotXY * f10h), (double)f6, (double)f8);
		tessellator.addVertexWithUV((double)(f11 + rotX * f10w - rotYZ * f10h), (double)(f12 - rotXZ * f10h), (double)(f13 + rotZ * f10w - rotXY * f10h), (double)f6, (double)f9);
		
		tessellator.draw();
		tessellator.startDrawingQuads();
		Minecraft.getMinecraft().renderEngine.func_110577_a(Dota2Particle.minecraftParticles);
	}

}
