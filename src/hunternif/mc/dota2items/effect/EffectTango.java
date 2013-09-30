package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleTango;
import hunternif.mc.dota2items.core.buff.Buff;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectTango extends ContinuousEffect {

	public EffectTango(World world) {
		super(world);
	}
	public EffectTango(Entity entity) {
		super(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void perform() {
		Random rand = new Random();
		if (entity.ticksExisted % (1 + rand.nextInt(3)) == 0) {
			World world = Minecraft.getMinecraft().theWorld;
			EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
			double x = entity.posX + (rand.nextDouble()*2-1)*0.5;
			double y = entity.posY - entity.yOffset + entity.height - rand.nextDouble()*0.5;
			double z = entity.posZ + (rand.nextDouble()*2-1)*0.5;
			EntityFX particle = new ParticleTango(entity.worldObj, x, y, z);
			effectRenderer.addEffect(particle);
		}
	}
	@Override
	public Buff getEffectProducingBuff() {
		return Buff.tango;
	}

}
