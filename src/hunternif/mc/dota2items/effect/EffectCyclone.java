package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.FXCycloneRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
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
		if (inst.data != null && inst.data.length >= 2) {
			float yaw = (Float) inst.data[0];
			float pitch = (Float) inst.data[1];
			EntityFX effect = new FXCycloneRing(world, inst.x, inst.y, inst.z, yaw, pitch, effectRenderer);
			if (inst.data.length > 2) {
				float alpha = (Float) inst.data[2];
				effect.setAlphaF(alpha);
			}
			effectRenderer.addEffect(effect);
		}
	}

}
