package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticlePhase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectPhase extends ContinuousEffect {

	public EffectPhase(World world) {
		super(world);
	}
	public EffectPhase(Entity entity) {
		super(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void perform() {
		EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
		double x = entity.posX;
		double y = entity.posY - entity.yOffset + 0.1;
		double z = entity.posZ;
		EntityFX particle = new ParticlePhase(entity.worldObj, x, y, z);
		effectRenderer.addEffect(particle);
	}
}
