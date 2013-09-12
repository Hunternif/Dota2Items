package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleMiss;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectMiss extends Effect {
	public EffectMiss(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void perform(EffectInstance inst) {
		World world = Minecraft.getMinecraft().theWorld;
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		Random rand = new Random();
		double x = ((Number) inst.data[0]).doubleValue() + (rand.nextDouble() - 0.5)*0.7;
		double y = ((Number) inst.data[1]).doubleValue() + (rand.nextDouble() - 0.5)*0.7;
		double z = ((Number) inst.data[2]).doubleValue();
		EntityFX particle = new ParticleMiss(world, x, y, z);
		effectRenderer.addEffect(particle);
	}

	@Override
	public Object[] readInstanceData(ByteArrayDataInput in) {
		Object[] data = new Object[3];
		for (int i = 0; i < 3; i++) {
			data[i] = in.readFloat();
		}
		return data;
	}

	@Override
	public void writeInstanceData(Object[] data, ByteArrayDataOutput out) {
		for (int i = 0; i < 3; i++) {
			out.writeFloat(((Number)data[i]).floatValue());
		}
	}

}
