package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWrapper extends Entity {
	private static final String TAG_WRAPPED_ENTITY_ID = "wrappedEntityID";
	private static final String TAG_AGE = "wrapperAge";
	private static final String TAG_MAX_AGE = "wrapperMaxAge";
	
	public Entity entity;
	public long age;
	public long maxAge;
	
	public EntityWrapper(World world) {
		super(world);
	}
	
	public EntityWrapper(Entity entity) {
		this(entity.worldObj);
		this.entity = entity;
		if (entity != null) {
			setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		}
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		int wrappedEntityID = tag.getInteger(TAG_WRAPPED_ENTITY_ID);
		this.entity = this.worldObj.getEntityByID(wrappedEntityID);
		this.age = tag.getLong(TAG_AGE);
		this.maxAge = tag.getLong(TAG_MAX_AGE);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger(TAG_WRAPPED_ENTITY_ID, entity == null ? -1 : entity.entityId);
		tag.setLong(TAG_AGE, age);
		tag.setLong(TAG_MAX_AGE, maxAge);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (entity!= null) {
			setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		}
		if (++age > maxAge) {
			setDead();
		}
	}
}
