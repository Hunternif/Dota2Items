package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class Dota2Particle extends EntityFX {
	private static float ICON_U_WIDTH = 1f/16f;

	protected Dota2Particle(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	public Dota2Particle(World world, double x, double y, double z,
			double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
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
		Minecraft.getMinecraft().renderEngine.bindTexture("/mods/"+Dota2Items.ID+"/textures/particles.png");
		tessellator.startDrawingQuads();
		tessellator.setBrightness(getBrightnessForRender(partialTick));
		super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
		tessellator.draw();
		tessellator.startDrawingQuads();
		Minecraft.getMinecraft().renderEngine.bindTexture("/particles.png");
	}

}
