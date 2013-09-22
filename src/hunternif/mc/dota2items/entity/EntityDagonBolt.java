package hunternif.mc.dota2items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityDagonBolt extends Entity {
	public static int maxAge = 6;
	
	public double startX;
	public double startY;
	public double startZ;
	public double endX;
	public double endY;
	public double endZ;
	
	private double length;

	public EntityDagonBolt(World world) {
		super(world);
		ignoreFrustumCheck = true;
	}
	
	public EntityDagonBolt(World world,
			double startX, double startY, double startZ,
			double endX, double endY, double endZ) {
		super(world);
		setPosition(startX, startY, startZ);
		setBoltCoords(startX, startY, startZ, endX, endY, endZ);
	}
	
	public void setBoltCoords(double startX, double startY, double startZ,
			double endX, double endY, double endZ) {
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
		length = Math.sqrt(
			(endX - startX)*(endX - startX) +
			(endY - startY)*(endY - startY) +
			(endZ - startZ)*(endZ - startZ));
		double lengthXZ = Math.sqrt(
				(endX - startX)*(endX - startX) +
				(endZ - startZ)*(endZ - startZ));
		double pitch = Math.atan2(endY - startY, lengthXZ) / Math.PI * 180;
		double yaw = -Math.atan2(endZ - startZ, endX - startX) / Math.PI * 180;
		setRotation((float)yaw, (float)pitch); 
	}
	
	public double getLength() {
		return length;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > maxAge) {
			setDead();
		}
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
}
