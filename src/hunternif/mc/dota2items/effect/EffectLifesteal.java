package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleLifesteal;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectLifesteal extends EntityEffect {
	public static final int MAX_PARTICLES = 20;

	public EffectLifesteal(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void perform(Entity entity, Object ... data) {
		World world = Minecraft.getMinecraft().theWorld;
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		Random rand = new Random();
		for (int i = 0; i < MAX_PARTICLES; i++) {
			float yaw = (rand.nextFloat()*2.0F - 1.0F) * (float) Math.PI;
			float pitch = (rand.nextFloat() - 0.5F) * (float) Math.PI;
			double distance = rand.nextDouble() * 0.3D + 0.2D;
			double cosYaw = (double) MathHelper.cos(yaw);
			double sinYaw = (double) MathHelper.sin(yaw);
			double cosPitch = (double) MathHelper.cos(pitch);
			double sinPitch = (double) MathHelper.sin(pitch);
			double rX = -sinYaw*cosPitch * distance;
			double rZ = cosYaw*cosPitch * distance;
			double rY = -sinPitch * distance;
			/*double velX = -sinYaw*cosPitch / (distance) * 0.05D;
			double velZ = cosYaw*cosPitch / (distance) * 0.05D;
			double velY = -sinPitch / (distance) * 0.05D;*/
			EntityFX particle = new ParticleLifesteal(world,
					entity.posX + rX, entity.posY - entity.yOffset + 1 + rY, entity.posZ + rZ, distance);
			effectRenderer.addEffect(particle);
		}
	}
}
