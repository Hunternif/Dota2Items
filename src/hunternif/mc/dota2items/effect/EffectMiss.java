package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleMiss;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectMiss extends EntityEffect {
	public EffectMiss(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void perform(Entity entity, Object ... data) {
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		Random rand = new Random();
		double x = entity.posX + (rand.nextDouble() - 0.5)*0.7;
		double y = entity.posY - entity.yOffset + 1.5;
		double z = entity.posZ + (rand.nextDouble() - 0.5)*0.7;
		EntityFX particle = new ParticleMiss(entity.worldObj, x, y, z);
		effectRenderer.addEffect(particle);
	}
}
