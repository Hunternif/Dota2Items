package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectForce extends Effect {
	public static final int puffsPerBlock = 2;
	private static final double trailStep = 1 / ((double) puffsPerBlock);
	
	public EffectForce(int id) {
		super(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void perform(EffectInstance inst) {
		double x = ((Number) inst.data[0]).doubleValue();
		double y = ((Number) inst.data[1]).doubleValue();
		double z = ((Number) inst.data[2]).doubleValue();
		double dx = ((Number) inst.data[3]).doubleValue();
		double dy = ((Number) inst.data[4]).doubleValue();
		double dz = ((Number) inst.data[5]).doubleValue();
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		double d = Math.sqrt(dx*dx + dy*dy + dz*dz);
		for (double i = 0; i < d; i += trailStep) {
			EntityFX particle = new ParticleDust(Minecraft.getMinecraft().theWorld,
					x + dx * i/d,
					y + dy * i/d,
					z + dz * i/d, 0, 0, 0);
			effectRenderer.addEffect(particle);
		}
	}

	@Override
	public Object[] readInstanceData(ByteArrayDataInput in) {
		return null;
	}

	@Override
	public void writeInstanceData(Object[] data, ByteArrayDataOutput out) {}
}
