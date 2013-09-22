package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.entity.EntityDagonBolt;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderDagonBolt extends Render {
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		EntityDagonBolt bolt = (EntityDagonBolt) entity;
		tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
        tessellator.setColorRGBA_F(1, 1, 0.9f, 0.5f);
        tessellator.addVertex(bolt.startX, bolt.startY, bolt.startZ);
        tessellator.addVertex((bolt.startX + bolt.endX)/2 - 0.1, (bolt.startY + bolt.endY)/2, (bolt.startZ + bolt.endZ)/2 - 0.1);
        tessellator.addVertex((bolt.startX + bolt.endX)/2 + 0.1, (bolt.startY + bolt.endY)/2, (bolt.startZ + bolt.endZ)/2 + 0.1);
        tessellator.addVertex(bolt.endX, bolt.endY, bolt.endZ);
        tessellator.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return null;
	}
}
