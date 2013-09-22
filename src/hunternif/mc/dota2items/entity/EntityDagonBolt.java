package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDagonBolt extends Entity {
	public static int maxAge = 40;
	
	public double startX;
	public double startY;
	public double startZ;
	public double endX;
	public double endY;
	public double endZ;

	public EntityDagonBolt(World world,
			double startX, double startY, double startZ,
			double endX, double endY, double endZ) {
		super(world);
		setPosition(startX, startY, startZ);
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > maxAge) {
			setDead();
		}
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
	
	/*@Override
	public boolean isInRangeToRenderVec3D(Vec3 par1Vec3) {
		return true;
	}
	
	@Override
	public boolean isInRangeToRenderDist(double par1) {
		return true;
	}*/
}
