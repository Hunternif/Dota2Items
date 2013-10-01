package hunternif.mc.dota2items.effect;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EffectClarity extends ContinuousEffect {
	public EffectClarity(World world) {
		super(world);
	}
	public EffectClarity(Entity entity) {
		super(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void perform() {}
}
