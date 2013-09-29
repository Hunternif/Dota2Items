package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleMiss;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectMiss extends EntityEffect {
	public EffectMiss(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void perform(Entity entity, Object ... data) {
		World world = Minecraft.getMinecraft().theWorld;
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		Random rand = new Random();
		double x = entity.posX + (rand.nextDouble() - 0.5)*0.7;
		double y = entity.posY - entity.yOffset + 1.5;
		double z = entity.posZ + (rand.nextDouble() - 0.5)*0.7;
		EntityFX particle = new ParticleMiss(world, x, y, z);
		effectRenderer.addEffect(particle);
	}
}
