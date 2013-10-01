package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWrapper extends Entity {
	protected Entity entity;
	
	public EntityWrapper(World world) {
		super(world);
	}
	
	public EntityWrapper(Entity entity) {
		this(entity.worldObj);
		setEntity(entity);
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
		if (entity != null) {
			setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		}
	}
	public Entity getEntity() {
		return entity;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (entity!= null) {
			setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		}
	}
}
