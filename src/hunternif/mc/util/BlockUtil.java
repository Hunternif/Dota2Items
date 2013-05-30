package hunternif.mc.util;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public final class BlockUtil {
	//          0
	//         00
	//        ... <lookAbove> blocks up
	// ray ->  0#
	//
	// (0 = air, # = block that the ray hit)
	public static boolean isReachableAirAbove(World world, int side, int x, int y, int z, int lookAbove) {
		// First, look if there're 2 blocks of air above the hit block within the height of <lookAbove>
		int airFound = 0;
		int yOfAirFound = -1;
		for (int dy = 1; dy <= lookAbove + 1; dy++) {
			if (!world.getBlockMaterial(x, y + dy, z).isSolid()) {
				airFound++;
				if (airFound == 2) {
					yOfAirFound = y + dy - 1;
					break;
				}
			} else {
				airFound = 0;
				yOfAirFound = -1;
			}
		}
		if (airFound < 2 || yOfAirFound == -1) {
			return false;
		}
		
		// Ok, found enough air above.
		// Let's see if it is reachable from the given side
		int sideX = x;
		int sideZ = z;
		switch(side) {
		case SideHit.NORTH:
			sideX--;
			break;
		case SideHit.SOUTH:
			sideX++;
			break;
		case SideHit.EAST:
			sideZ--;
			break;
		case SideHit.WEST:
			sideZ++;
			break;
		}
		for (int sideY = y; sideY <= yOfAirFound; sideY++) {
			if (world.getBlockMaterial(sideX, sideY, sideZ).isSolid()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isSolid(World world, Vec3 vec) {
		int x = MathHelper.floor_double(vec.xCoord);
		int y = MathHelper.floor_double(vec.yCoord);
		int z = MathHelper.floor_double(vec.zCoord);
		return world.getBlockMaterial(x, y, z).isSolid();
	}
}
