package hunternif.mc.dota2items.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class ParticlePhase extends Dota2Particle {
	
	public ParticlePhase(World world, double x, double y, double z) {
		super(world, x, y, z);
		setTexturePositions(13, 0);
		this.particleMaxAge = 5;
	}
	
	@Override
	protected float scaleAtAge(float ageFraq) {
		return 6*(1.3f-ageFraq);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		setRBGColorF(particleAlpha, particleAlpha, particleAlpha);
	}
	
	@Override
	public void renderParticle(Tessellator tessellator, float partialTick,
			float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		GL11.glDepthMask(false);
		super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
		GL11.glDepthMask(true);
	}

}
