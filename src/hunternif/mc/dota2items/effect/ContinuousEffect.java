package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.entity.EntityWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContinuousEffect extends EntityWrapper {
	public ContinuousEffect(World world) {
		super(world);
	}
	public ContinuousEffect(Entity entity) {
		super(entity);
	}
	
	public abstract Buff getEffectProducingBuff();

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (getEffectProducingBuff() != null) {
			EntityStats stats = Dota2Items.stats.getEntityStats(entity);
			if (stats == null || !stats.hasBuff(getEffectProducingBuff())) {
				setDead();
			}
		}
		if (entity != null && entity.worldObj.isRemote) {
			perform();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void perform();
}
