package hunternif.mc.moses.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class MathUtil {
	public static final float PI_1_4 = ((float) Math.PI) * 0.25F;
	public static final float PI_3_4 = ((float) Math.PI) * 0.75F;
	public static final float PI_5_4 = ((float) Math.PI) * 1.25F;
	public static final float PI_7_4 = ((float) Math.PI) * 1.75F;
	public static final float _2_PI = ((float) Math.PI) * 2F;
	
	//NOTE: generate table of values like MathHelper.sin and cos
	public static float tan(float angle) {
		float sin = MathHelper.sin(angle);
		float cos = MathHelper.cos(angle);
		if (cos == 0 && sin > 0) return Float.MAX_VALUE;
		else if (cos == 0 && sin < 0) return -Float.MAX_VALUE;
		return sin / cos;
	}
	
	public static float cot(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		if (sin == 0 && cos > 0) return Float.MAX_VALUE;
		else if (sin == 0 && cos < 0) return -Float.MAX_VALUE;
		return cos / sin;
	}
	
	public static int clampInt(int value, int bound1, int bound2) {
		if (bound1 <= bound2)
			return MathHelper.clamp_int(value, bound1, bound2);
		else
			return MathHelper.clamp_int(value, bound2, bound1);
	}
	
	public static Vec3 crossProduct(Vec3 vec1, Vec3 vec2) {
		return vec1.myVec3LocalPool.getVecFromPool(
				vec1.yCoord * vec2.zCoord - vec1.zCoord * vec2.yCoord,
				vec1.zCoord * vec2.xCoord - vec1.xCoord * vec2.zCoord,
				vec1.xCoord * vec2.yCoord - vec1.yCoord * vec2.xCoord);
	}
}
