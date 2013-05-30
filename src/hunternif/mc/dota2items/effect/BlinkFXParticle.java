package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class BlinkFXParticle extends EntityFX {
	public static final int SIZES = 5;
	private static final float ICON_U_WIDTH = 1/(float)SIZES;
	
	private int stage = 0;
	
	protected BlinkFXParticle(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.rand = new Random((long)((x + y + z)*100000));
		stage = world.rand.nextInt(SIZES);
		this.particleScale = (this.rand.nextFloat() * 0.3F + 0.7F) * 2.0F;
        this.particleMaxAge = (int)(12.0F / (this.rand.nextFloat() * 0.5F + 0.5F));
        this.motionX = velX + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
        this.motionY = velY + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
        this.motionZ = velZ + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.04F);
        this.setAlphaF(1);
	}
	
	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		//TODO unify this method for other custom particles
		Minecraft.getMinecraft().renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/particles/blinkDagger_particles.png");
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float u = (float)stage*ICON_U_WIDTH;
		float scale = 0.1F * this.particleScale;
		float xPos = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
        float yPos = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
        float zPos = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);
        float colorIntensity = 1.0F;
        tessellator.setColorRGBA_F(this.particleRed * colorIntensity, this.particleGreen * colorIntensity, this.particleBlue * colorIntensity, this.particleAlpha);
        tessellator.addVertexWithUV((double)(xPos - rotX * scale - rotYZ * scale), (double)(yPos - rotXZ * scale), (double)(zPos - rotZ * scale - rotXY * scale), u+ICON_U_WIDTH, 1);
        tessellator.addVertexWithUV((double)(xPos - rotX * scale + rotYZ * scale), (double)(yPos + rotXZ * scale), (double)(zPos - rotZ * scale + rotXY * scale), u+ICON_U_WIDTH, 0);
        tessellator.addVertexWithUV((double)(xPos + rotX * scale + rotYZ * scale), (double)(yPos + rotXZ * scale), (double)(zPos + rotZ * scale + rotXY * scale), u, 0);
        tessellator.addVertexWithUV((double)(xPos + rotX * scale - rotYZ * scale), (double)(yPos - rotXZ * scale), (double)(zPos + rotZ * scale - rotXY * scale), u, 1);
	}
	
	public void onUpdate() {
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }
        if (stage < SIZES-1 && this.rand.nextInt((stage+4)/2) == 1) {
        	stage++;
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

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
	}
}