package hunternif.mc.moses.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;

public class IntVec3 {
	public int x;
	public int y;
	public int z;
	
	public IntVec3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public IntVec3(Vec3 vec3) {
		x = MathHelper.floor_double(vec3.xCoord);
		y = MathHelper.floor_double(vec3.yCoord);
		z = MathHelper.floor_double(vec3.zCoord);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public IntVec3 copy() {
		return new IntVec3(x, y, z);
	}
	
	public double distanceTo(IntVec3 intVec3) {
		double x1 = x;
		double y1 = y;
		double z1 = z;
		double x2 = intVec3.x;
		double y2 = intVec3.y;
		double z2 = intVec3.z;
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) + (z1-z2)*(z1-z2));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntVec3))
			return false;
		IntVec3 vec = (IntVec3) obj;
		return vec.x == x && vec.y == y && vec.z == z;
	}
	
	public Vec3 toVec3(Vec3Pool pool) {
		return pool.getVecFromPool(x, y, z);
	}
}
