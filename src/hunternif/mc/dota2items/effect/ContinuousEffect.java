package hunternif.mc.dota2items.effect;

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

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (entity != null && entity.worldObj.isRemote) {
			perform();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void perform();
}
