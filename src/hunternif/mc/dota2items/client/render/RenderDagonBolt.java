package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.effect.EntityDagonBolt;
import hunternif.mc.dota2items.util.RenderHelper;
import hunternif.mc.dota2items.util.Vec3Chain.Segment;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class RenderDagonBolt extends RenderEntity {
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		EntityDagonBolt bolt = (EntityDagonBolt) entity;
		float ageFraq = ((float)bolt.ticksExisted + partialTick) / (float)bolt.maxAge;
		double innerWidth = (double)bolt.level * 0.025 * (1d - ageFraq);
		double outerWidth = innerWidth * 4;
		float innerAlpha = 0.3f;
		float outerAlpha = 0.2f;
		if (ageFraq > 0.7) {
			innerAlpha *= 1f - (ageFraq - 0.7f)/0.3f;
			outerAlpha *= 1f - (ageFraq - 0.7f)/0.3f;
		}
		if (bolt.chain != null) {
			for (Segment seg : bolt.chain.segments) {
				GL11.glRotatef(seg.getYaw(), 0, 1, 0);
				GL11.glRotatef(seg.getPitch(), 0, 0, 1);
				RenderHelper.renderColorBox(innerWidth, innerWidth, seg.getLength()+0.02, 0xFFFCA2, innerAlpha);
				RenderHelper.renderColorBox(outerWidth, outerWidth, seg.getLength()+0.05, 0xff0000, outerAlpha);
				GL11.glTranslated(seg.getLength(), 0, 0);
				GL11.glRotatef(-seg.getPitch(), 0, 0, 1);
				GL11.glRotatef(-seg.getYaw(), 0, 1, 0);
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
