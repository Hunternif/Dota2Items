package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWrapper extends Entity {
	private static final String TAG_WRAPPED_ENTITY_ID = "wrappedEntityID";
	
	public Entity entity;
	
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
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger(TAG_WRAPPED_ENTITY_ID, entity == null ? -1 : entity.entityId);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (entity!= null) {
			setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		}
	}
}
