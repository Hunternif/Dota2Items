package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.FXCycloneRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectCyclone extends Effect {

	public EffectCyclone(int id) {
		super(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void perform(EffectInstance inst) {
		World world = Minecraft.getMinecraft().theWorld;
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		double x = (Integer) inst.data[0] + 0.5;
		double y = (Integer) inst.data[1];
		double z = (Integer) inst.data[2] + 0.5;
		float yaw = (Float) inst.data[3];
		float pitch = (Float) inst.data[4];
		float alpha = (Float) inst.data[5];
		EntityFX effect = new FXCycloneRing(world, x, y, z, yaw, pitch, alpha, effectRenderer);
		if (inst.data.length > 2) {
			alpha = (Float) inst.data[2];
			effect.setAlphaF(alpha);
		}
		effectRenderer.addEffect(effect);
	}
	
	@Override
	public Object[] readInstanceData(ByteArrayDataInput in) {
		Object[] data = new Object[3];
		for (int i = 0; i < 3; i++) {
			data[i] = in.readInt();
		}
		data[3] = in.readFloat();
		data[4] = in.readFloat();
		return data;
	}

	@Override
	public void writeInstanceData(Object[] data, ByteArrayDataOutput out) {
		for (int i = 0; i < 3; i++) {
			out.writeInt((Integer)data[i]);
		}
		out.writeFloat((Float)data[3]);
		out.writeFloat((Float)data[4]);
	}

}