package hunternif.mc.dota2items.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class BlockUtil {
	
	public static final Set<Material> groundMaterials = new HashSet<Material>();
	static {
		groundMaterials.add(Material.grass);
		groundMaterials.add(Material.ground);
		groundMaterials.add(Material.sand);
		groundMaterials.add(Material.rock);
		groundMaterials.add(Material.clay);
		groundMaterials.add(Material.ice);
		groundMaterials.add(Material.water);
	}
	
	public static final Map<Material, Block> surfaceToGroundMap = new HashMap<Material, Block>();
	static {
		surfaceToGroundMap.put(Material.grass, Block.dirt);
		surfaceToGroundMap.put(Material.ground, Block.dirt);
		surfaceToGroundMap.put(Material.sand, Block.sand);
		surfaceToGroundMap.put(Material.clay, Block.sand);
		surfaceToGroundMap.put(Material.rock, Block.stone);
		surfaceToGroundMap.put(Material.water, Block.fence); // Stilt house
		surfaceToGroundMap.put(Material.ice, Block.ice);
	}
	
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
	
	public static IntVec3 findSurfaceUpward(World world, int x, int y, int z) {
		while (!world.isAirBlock(x, y, z) && y < world.getHeight()) {
			y++;
		}
		if (y >= world.getHeight()) {
			return null;
		}
		return new IntVec3(x, y, z);
	}
	
	/** Finds first block below which is considered ground material. */
	public static IntVec3 findSurfaceDownward(World world, int x, int y, int z) {
		while (y > 0) {
			Material material = world.getBlockMaterial(x, y, z);
			if (groundMaterials.contains(material)) {
				break;
			} else {
				y--;
			}
		}
		if (y == 0) {
			return null;
		}
		return new IntVec3(x, y + 1, z);
	}
	
	public static boolean isSolid(World world, Vec3 vec) {
		int x = MathHelper.floor_double(vec.xCoord);
		int y = MathHelper.floor_double(vec.yCoord);
		int z = MathHelper.floor_double(vec.zCoord);
		return world.getBlockMaterial(x, y, z).isSolid();
	}
	
	public static boolean areCoordinatesWithinChunk(Chunk chunk, ChunkCoordinates coords) {
		int dx = (coords.posX >> 4) - chunk.xPosition;
		int dz = (coords.posZ >> 4) - chunk.zPosition;
		return dx == 0 && dz == 0;
	}
	
	public static void elevateGroundTo(World world, int x, int y, int z) {
		int summit = y;
		Material material = null;
		Block block = null;
		while (y > 0) {
			material = world.getBlockMaterial(x, y, z);
			block = surfaceToGroundMap.get(material);
			if (block != null) {
				break;
			} else {
				y--;
			}
		}
		if (y == 0 || block == null) {
			return;
		}
		while (y < summit) {
			y++;
			world.setBlock(x, y, z, block.blockID);
		}
	}
}
