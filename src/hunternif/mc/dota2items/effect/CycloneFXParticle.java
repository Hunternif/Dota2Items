package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class CycloneFXParticle extends EntityFX {
	//public static int invisibleAge = 1;
	public static int expandAge = 10;

	protected CycloneFXParticle(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
	}
	
	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		Minecraft.getMinecraft().renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/particles/puff.png");
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float scale = 0.1F * this.particleScale;
		float xPos = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
        float yPos = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
        float zPos = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);
        float colorIntensity = 1.0F;
        tessellator.setColorRGBA_F(this.particleRed * colorIntensity, this.particleGreen * colorIntensity, this.particleBlue * colorIntensity, this.particleAlpha);
        tessellator.addVertexWithUV((double)(xPos - rotX * scale - rotYZ * scale), (double)(yPos - rotXZ * scale), (double)(zPos - rotZ * scale - rotXY * scale), 1, 1);
        tessellator.addVertexWithUV((double)(xPos - rotX * scale + rotYZ * scale), (double)(yPos + rotXZ * scale), (double)(zPos - rotZ * scale + rotXY * scale), 1, 0);
        tessellator.addVertexWithUV((double)(xPos + rotX * scale + rotYZ * scale), (double)(yPos + rotXZ * scale), (double)(zPos + rotZ * scale + rotXY * scale), 0, 0);
        tessellator.addVertexWithUV((double)(xPos + rotX * scale - rotYZ * scale), (double)(yPos - rotXZ * scale), (double)(zPos + rotZ * scale - rotXY * scale), 0, 1);
		//GL11.glDepthMask(true);
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
