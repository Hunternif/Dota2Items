package hunternif.mc.dota2items.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public final class TreeUtil {
	/** Will cause a block update. */
	public static int FLAG_BLOCK_UPDATE = 1;
	/** Will send the change to clients (you almost always want this). */
	public static int FLAG_SEND_CHANGE_TO_CLIENTS = 2;
	/** Prevents the block from being re-rendered, if this is a client world. */
	public static int FLAG_PREVENT_REENTER = 4;
	
	/** The minimum height for a column of wooden blocks to count as a tree trunk. */
	public static int minTreeHeight = 3;
	
	///** Below this height threshold will delete blocks in smaller area. */
	//public static int treeHeightThreshold = 2;
	//public static int maxFoliageRadiusBelow = 1;
	//public static int maxFoliageRadiusAbove = 5;
	
	/** The index denotes height from the trunk base, the value denotes maximum foliage radius. */
	public static int[] maxRadiuses = {1, 3, 5};
	
	/**
	 * Used to look some levels above the tree top, in case the highest point
	 * is not directly above the trunk.
	 */
	public static int treeTopOverhead = 1;
	
	public static int otherTreeMaxFoliageRadius = 2;
	public static int otherTreeMaxFoliageHeight = 2;
	
	//public static int otherTreeMaxBranchRadius = 2;
	//public static int otherTreeMaxBranchHeight = 2;
	
	public static int maxTrunkRadius = 1;
	
	/** Only searches the given height BELOW, not above! */
	public static IntVec3 findWoodInBox(World world, int centerX, int topY, int centerZ, int height, int halfWidth) {
		for (int y = topY-height; y <= topY; y++) {
			for (int x = centerX-halfWidth; x <= centerX+halfWidth; x++) {
				for (int z = centerZ-halfWidth; z <= centerZ+halfWidth; z++) {
					if (Block.blocksList[world.getBlockId(x, y, z)] == Block.wood)
						return new IntVec3(x, y, z);
				}
			}
		}
		return null;
	}
	
	/** Only searches the given height BELOW, not above! */
	public static IntVec3 findTreeTrunkInBox(World world, int centerX, int centerY, int centerZ, int height, int radius) {
		for (int y = centerY-height; y <= centerY; y++) {
			IntVec3 trunkBase = findTreeTrunkInSquare(world, centerX, y, centerZ, radius);
			if (trunkBase != null && isBranch(world, trunkBase, new IntVec3(centerX, centerY, centerZ))) {
				return trunkBase;
			}
		}
		return null;
	}
	
	public static IntVec3 findTreeTrunkInSquare(World world, int centerX, int centerY, int centerZ, int halfWidth) {
		for (int radius = 0; radius <= halfWidth; radius ++) {
			IntVec3 trunkBase = findTreeTrunkInRing(world, centerX - radius, centerY, centerZ - radius, radius*2+1);
			if (trunkBase != null && isBranch(world, trunkBase, new IntVec3(centerX, centerY, centerZ))) {
				return trunkBase;
			}
		}
		return null;
	}
	
	/**
	 * Actually it's a square ring in the XZ plane, with its top left corner at
	 * (x, y, z). Radius 1 denotes a single block, radius 2 - a 2x2 square etc.
	 */
	public static IntVec3 findTreeTrunkInRing(World world, int leftX, int y, int topZ, int radius) {
		if (radius <= 1) {
			int baseY = getTreeTrunkBaseY(world, leftX, y, topZ);
			if (baseY > 0) {
				return new IntVec3(leftX, baseY, topZ);
			}
		}
		// Traverse the ring clockwise:
		// top edge
		for (int x = leftX; x < leftX+radius-1; x++) {
			int z = topZ;
			int baseY = getTreeTrunkBaseY(world, x, y, z);
			if (baseY > 0) {
				return new IntVec3(x, baseY, z);
			}
		}
		// right edge
		for (int z = topZ; z < topZ+radius-1; z++) {
			int x = leftX+radius-1;
			int baseY = getTreeTrunkBaseY(world, x, y, z);
			if (baseY > 0) {
				return new IntVec3(x, baseY, z);
			}
		}
		// bottom edge
		for (int x = leftX+radius-1; x > leftX; x--) {
			int z = topZ+radius-1;
			int baseY = getTreeTrunkBaseY(world, x, y, z);
			if (baseY > 0) {
				return new IntVec3(x, baseY, z);
			}
		}
		// left edge
		for (int z = topZ+radius-1; z > topZ; z--) {
			int x = leftX;
			int baseY = getTreeTrunkBaseY(world, x, y, z);
			if (baseY > 0) {
				return new IntVec3(x, baseY, z);
			}
		}
		return null;
	}
	
	/** Returns true, if {@code canReach} returned true for any block in the trunk. */
	public static boolean isBranch(World world, IntVec3 trunk, IntVec3 leaves) {
		//Traverse the trunk up:
		int y = trunk.y;
		Block block = Block.blocksList[world.getBlockId(trunk.x, y, trunk.z)];
		while (block == Block.wood) {
			if (canReach(world, leaves, new IntVec3(trunk.x, y, trunk.z)))
				return true;
			y++;
			block = Block.blocksList[world.getBlockId(trunk.x, y, trunk.z)];
		}
		return false;
	}
	
	/**
	 * Returns true if there is a somewhat direct path of wood and leaves
	 * from p1 to p2.
	 */
	public static boolean canReach(World world, IntVec3 p1, IntVec3 p2) {
		Vec3 source = p1.toVec3(world.getWorldVec3Pool());
		Vec3 dest = p2.toVec3(world.getWorldVec3Pool());
		Vec3 ray = dest.addVector(-source.xCoord, -source.yCoord, -source.zCoord).normalize();
		Vec3 pos = source;
		int dx = p2.x > p1.x ? 1 : -1;
		int dy = p2.y > p1.y ? 1 : -1;
		int dz = p2.z > p1.z ? 1 : -1;
		while (pos.distanceTo(dest) > 1) {
			pos = pos.addVector(ray.xCoord, ray.yCoord, ray.zCoord);
			int x = MathHelper.floor_double(pos.xCoord);
			int y = MathHelper.floor_double(pos.yCoord);
			int z = MathHelper.floor_double(pos.zCoord);
			if (isWoodOrLeaves(world, x, y, z) ||
				isWoodOrLeaves(world, x+dx, y, z) ||
				isWoodOrLeaves(world, x, y, z+dz) ||
				isWoodOrLeaves(world, x+dx, y, z+dz) ||
				isWoodOrLeaves(world, x, y+dy, z) ||
				isWoodOrLeaves(world, x+dx, y+dy, z) ||
				isWoodOrLeaves(world, x, y+dy, z+dz) ||
				isWoodOrLeaves(world, x+dx, y+dy, z+dz))
				continue;
			else
				return false;
		}
		return true;
	}
	
	/**
	 * If the block at (x,y,z) is wood and it is part of a wooden column of
	 * a  predefined minimum height, then this method returns the lowest y
	 * coordinate of this column. Otherwise returns -1.
	 */
	public static int getTreeTrunkBaseY(World world, int x, int y, int z) {
		int base = y;
		// Go down to find the base
		while (Block.blocksList[world.getBlockId(x, base, z)] == Block.wood) {
			base --;
		}
		base ++;
		
		// Go up to measure height
		int height = 0;
		while (Block.blocksList[world.getBlockId(x, base+height, z)] == Block.wood) {
			height++;
		}
		
		if (height >= minTreeHeight) {
			// Yep, it's a long wood
			return base;
		} else {
			// That's what she said!
			return -1;
		}
	}
	
	/**
	 * Recursively removes tree with its trunk base specified.
	 * This method will not look below the specified point!
	 */
	//NOTE potential issue: suppose there's a little bush with leaves within
	// 5-block proximity of a regular tree. If I try to eat this little bush,
	// the tree will disappear instead.
	public static void removeTree(World world, IntVec3 trunkBase, boolean placeSapling) {
		int woodMetadata = world.getBlockMetadata (trunkBase.x, trunkBase.y, trunkBase.z);
		
		// Brace yourselves, this is a recursive method!
		int x = trunkBase.x;		
		int y = trunkBase.y;
		int z = trunkBase.z;
		
		for (int i = 0; i < maxRadiuses.length-1; i++) {
			int maxRadius = maxRadiuses[i];
			for (int radius = 0; radius <= maxRadius; radius ++) {
				clearTreeBlocksInRing(world, x - radius, y, z - radius, radius*2+1, radius > maxTrunkRadius);
			}
			y++;
		}
		
		if (placeSapling) {
			world.setBlock(trunkBase.x, trunkBase.y, trunkBase.z, Block.sapling.blockID, woodMetadata, FLAG_SEND_CHANGE_TO_CLIENTS);
		}
				
		int lastMaxRadius = maxRadiuses[maxRadiuses.length-1];
		while (y < world.getHeight()) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if (block != Block.wood && block != Block.leaves &&
					block != Block.snow && !(block instanceof BlockFlower)) {
				break;
			} else {
				for (int radius = 0; radius <= lastMaxRadius; radius ++) {
					clearTreeBlocksInRing(world, x - radius, y, z - radius, radius*2+1, radius > maxTrunkRadius);
				}
			}
			y++;
		}
		// Just in case the highest point of the tree was not directly above
		// the trunk, clear some "overhead" levels above.
		for (int overhead = 0; (overhead < treeTopOverhead) && (overhead + y < world.getHeight()); overhead++) {
			for (int radius = 0; radius <= lastMaxRadius; radius ++) {
				clearTreeBlocksInRing(world, x - radius, y + overhead, z - radius, radius*2+1, radius > maxTrunkRadius);
			}
		}
	}
	
	public static void clearTreeBlocksInRing(World world, int leftX, int y, int topZ, int radius, boolean respectOtherTrees) {
		if (radius <= 1) {
			clearTreeBlock(world, leftX, y, topZ);
			return;
		}
		// Traverse the ring clockwise:
		// top edge
		for (int x = leftX; x < leftX+radius-1; x++) {
			int z = topZ;
			recursiveClearTreeBlock(world, x, y, z, respectOtherTrees);
		}
		// right edge
		for (int z = topZ; z < topZ+radius-1; z++) {
			int x = leftX+radius-1;
			recursiveClearTreeBlock(world, x, y, z, respectOtherTrees);
		}
		// bottom edge
		for (int x = leftX+radius-1; x > leftX; x--) {
			int z = topZ+radius-1;
			recursiveClearTreeBlock(world, x, y, z, respectOtherTrees);
		}
		// left edge
		for (int z = topZ+radius-1; z > topZ; z--) {
			int x = leftX;
			recursiveClearTreeBlock(world, x, y, z, respectOtherTrees);
		}
	}
	
	/**
	 * @param respectOtherTrees if true, then any found tree trunks and their
	 * 							respective foliage will not be removed.
	 */
	private static void recursiveClearTreeBlock(World world, int x, int y, int z, boolean respectOtherTrees) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null) {
			return;
		} else if (block == Block.wood) {
			if (!respectOtherTrees || findTreeTrunkInBox(world, x, y, z, otherTreeMaxFoliageHeight, otherTreeMaxFoliageRadius) == null) {
				// This is a branch of our tree or part of its wide trunk.
				removeTree(world, new IntVec3(x, y, z), false);
			}
		} else if (block == Block.leaves) {
			if (respectOtherTrees && findTreeTrunkInBox(world, x, y-1, z, otherTreeMaxFoliageHeight, otherTreeMaxFoliageRadius) != null) {
				// These leaves belong to a branch of another tree.
				return;
			} else {
				clearTreeBlock(world, x, y, z);
			}
		} else if (block == Block.vine || block == Block.cocoaPlant) {
			clearTreeBlock(world, x, y, z);
		}
	}
	
	/** Non-recursive, i.e. will not look around for other trees. */
	public static void clearTreeBlock(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null) {
			return;
		} else if (block == Block.wood || block == Block.leaves) {
			removeVine(world, x-1, y, z);
			removeVine(world, x+1, y, z);
			removeVine(world, x, y, z-1);
			removeVine(world, x, y, z+1);
			removeSnowAndFlowers(world, x, y+1, z);
			world.setBlock(x, y, z, 0);
		} else if (block == Block.vine) {
			removeVine(world, x, y, z);
		} else if (block == Block.cocoaPlant) {
			world.setBlock(x, y, z, 0);
		}
	}
	
	/** Removes vine from (x,y,z) down to its end. */
	public static void removeVine(World world, int x, int y, int z) {
		while (Block.blocksList[world.getBlockId(x, y, z)] == Block.vine) {
			world.setBlock(x, y, z, 0);
			y--;
		}
	}
	/**
	 * Clears the block at (x,y,z) if it is snow or a flower.
	 * According to Notch, mushrooms are flowers.
	 */
	public static void removeSnowAndFlowers(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == Block.snow || block instanceof BlockFlower) {
			world.setBlock(x, y, z, 0);
		}
	}
	
	public static boolean isWoodOrLeaves(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		return block == Block.wood || block == Block.leaves;
	}
}
