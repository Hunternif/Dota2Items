package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.client.particle.Dota2Particle;
import hunternif.mc.dota2items.effect.EffectClarity;
import hunternif.mc.dota2items.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class RenderClarityEffect extends RenderEntity {
	private static final int MAX_PARTICLES = 40;
	private static final float PARTICLE_ANGULAR_SPACING = MathUtil._2_PI / (float) MAX_PARTICLES;
	private static final float DELTA_HEIGHT = 0.2f;
	private static final float HEIGHT = 0.2f;
	
	private static final int COLOR = 0x6FA5FF;
	private static final float RED = (float)(COLOR >> 16 & 0xff)/256f;
	private static final float GREEN = (float)(COLOR >> 8 & 0xff)/256f;
	private static final float BLUE = (float)(COLOR & 0xff)/256f;
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
		EffectClarity effect = (EffectClarity) entity;
		if (effect.getEntity() == null) {
			return;
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		
		float f1 = ActiveRenderInfo.rotationX;
		float f2 = ActiveRenderInfo.rotationZ;
		float f3 = ActiveRenderInfo.rotationYZ;
		float f4 = ActiveRenderInfo.rotationXY;
		float f5 = ActiveRenderInfo.rotationXZ;
		
		float rotation = 0.16f*((float)effect.ticksExisted + partialTick);
		float radius = entity.width;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(Dota2Particle.minecraftParticles);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		
		for (float angle = 0; angle < MathUtil._2_PI; angle += PARTICLE_ANGULAR_SPACING) {
			float particleX = radius * MathHelper.cos(angle);
			float particleY = DELTA_HEIGHT * MathHelper.sin(rotation + angle) - effect.getEntity().yOffset + DELTA_HEIGHT + HEIGHT;
			float particleZ = radius * MathHelper.sin(angle);
			tessellator.setColorRGBA_F(RED, GREEN, BLUE, 0.5f*MathHelper.sin(angle + rotation/2));
			renderParticle(particleX, particleY, particleZ, partialTick, f1, f5, f2, f3, f4);
		}
		
		rotation += Math.PI;
		for (float angle = 0; angle < MathUtil._2_PI; angle += PARTICLE_ANGULAR_SPACING) {
			float particleX = radius * MathHelper.cos(angle);
			float particleY = DELTA_HEIGHT * MathHelper.sin(rotation + angle) - effect.getEntity().yOffset + DELTA_HEIGHT + HEIGHT;
			float particleZ = radius * MathHelper.sin(angle);
			tessellator.setColorRGBA_F(RED, GREEN, BLUE, 0.7f*MathHelper.sin(angle + rotation/2));
			renderParticle(particleX, particleY, particleZ, partialTick, f1, f5, f2, f3, f4);
		}
		tessellator.draw();

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private static void renderParticle(float posX, float posY, float posZ,
			float par2, float par3, float par4, float par5, float par6, float par7) {
		float f6 = 0.25F;
		float f7 = f6 + 0.25F;
		float f8 = 0.125F;
		float f9 = f8 + 0.25F;
		float size = 0.14f;
		Tessellator t = Tessellator.instance;
		t.addVertexWithUV((double)(posX - par3 * size - par6 * size), (double)(posY - par4 * size), (double)(posZ - par5 * size - par7 * size), (double)f7, (double)f9);
		t.addVertexWithUV((double)(posX - par3 * size + par6 * size), (double)(posY + par4 * size), (double)(posZ - par5 * size + par7 * size), (double)f7, (double)f8);
		t.addVertexWithUV((double)(posX + par3 * size + par6 * size), (double)(posY + par4 * size), (double)(posZ + par5 * size + par7 * size), (double)f6, (double)f8);
		t.addVertexWithUV((double)(posX + par3 * size - par6 * size), (double)(posY - par4 * size), (double)(posZ + par5 * size - par7 * size), (double)f6, (double)f9);
	}
}
