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
		double x = ((Number) inst.data[0]).doubleValue();
		double y = ((Number) inst.data[1]).doubleValue();
		double z = ((Number) inst.data[2]).doubleValue();
		float yaw = ((Number) inst.data[3]).floatValue();
		float pitch = ((Number) inst.data[4]).floatValue();
		float alpha = ((Number) inst.data[5]).floatValue();
		EntityFX effect = new FXCycloneRing(world, x, y, z, yaw, pitch, alpha, effectRenderer);
		effectRenderer.addEffect(effect);
	}
	
	@Override
	public Object[] readInstanceData(ByteArrayDataInput in) {
		Object[] data = new Object[3];
		for (int i = 0; i < 3; i++) {
			data[i] = in.readInt();
		}
		for (int i = 3; i < 6; i++) {
			data[i] = in.readFloat();
		}
		return data;
	}

	@Override
	public void writeInstanceData(Object[] data, ByteArrayDataOutput out) {
		for (int i = 0; i < 3; i++) {
			out.writeInt(((Number)data[i]).intValue());
		}
		for (int i = 3; i < 6; i++) {
			out.writeFloat(((Number)data[i]).floatValue());
		}
	}

}
