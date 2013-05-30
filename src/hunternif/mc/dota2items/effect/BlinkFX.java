package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlinkFX extends EntityFX {
	public static final int MAX_PARTICLES = 30;
	
	protected BlinkFX(World world, double x, double y, double z, EffectRenderer renderer) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		for (int i = 0; i < MAX_PARTICLES; i++) {
			float yaw = (rand.nextFloat()*2.0F - 1.0F) * (float) Math.PI;
			float pitch = (rand.nextFloat() - 0.5F) * (float) Math.PI;
			double distance = rand.nextDouble() * 0.3D + 0.7D;
			double cosYaw = (double) MathHelper.cos(yaw);
			double sinYaw = (double) MathHelper.sin(yaw);
			double cosPitch = (double) MathHelper.cos(pitch);
			double sinPitch = (double) MathHelper.sin(pitch);
			double rX = -sinYaw*cosPitch * distance;
			double rZ = cosYaw*cosPitch * distance;
			double rY = -sinPitch * distance;
			double velX = -sinYaw*cosPitch / (distance) * 0.05D;
			double velZ = cosYaw*cosPitch / (distance) * 0.05D;
			double velY = -sinPitch / (distance) * 0.05D;
			BlinkFXParticle particle = new BlinkFXParticle(world, posX + rX, posY + rY, posZ + rZ, velX, velY, velZ);
			renderer.addEffect(particle/*, Dota2Items.particlesDummyItem*/);
		}
	}
	
	// BlinkFX only spawns BlinkFXParticle's, it's not a visible effect itself.
	@Override
	public void renderParticle(Tessellator tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {}
}
