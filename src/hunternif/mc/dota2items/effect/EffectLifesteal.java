package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleLifesteal;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectLifesteal extends Effect {
	public static final int MAX_PARTICLES = 20;

	public EffectLifesteal(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void perform(EffectInstance inst) {
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
			double x = (Double) inst.data[0];
			double y = (Double) inst.data[1];
			double z = (Double) inst.data[2];
			EntityFX particle = new ParticleLifesteal(world, x + rX, y + rY, z + rZ, distance);
			effectRenderer.addEffect(particle);
		}
	}

	@Override
	public Object[] readInstanceData(ByteArrayDataInput in) {
		Object[] data = new Object[3];
		for (int i = 0; i < 3; i++) {
			data[i] = in.readDouble();
		}
		return data;
	}

	@Override
	public void writeInstanceData(Object[] data, ByteArrayDataOutput out) {
		for (int i = 0; i < 3; i++) {
			out.writeDouble((Double)data[i]);
		}
	}

}
