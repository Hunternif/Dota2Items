package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class TemporaryEntity extends Entity {
	
	public TemporaryEntity(World world) {
		super(world);
	}
	
	/** How long the entity will exist, in ticks. */
	public abstract int getDuration();
	
	/** How long the entity has existed relative to its total life span.
	 * @return a value ranging from 0.0f to 1.0f
	 */ 
	public float getDurationFraq(float partialTick) {
		if (getDuration() == 0) return 0;
		return ((float)ticksExisted + partialTick) / (float)getDuration();
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > getDuration()) {
			setDead();
		}
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

}
