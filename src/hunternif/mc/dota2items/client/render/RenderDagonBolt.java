package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.entity.EntityDagonBolt;
import hunternif.mc.dota2items.util.RenderHelper;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderDagonBolt extends RenderEntity {
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);

		EntityDagonBolt bolt = (EntityDagonBolt) entity;

		GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
		GL11.glRotatef(entity.rotationPitch, 0, 0, 1);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderHelper.renderColorBox(0.05, 0.05, bolt.getLength(), 0xFFFCA2, 0.5f);
		RenderHelper.renderColorBox(0.2, 0.2, bolt.getLength(), 0xff0000, 0.3f);
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return null;
	}
}
