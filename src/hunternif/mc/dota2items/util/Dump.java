package hunternif.mc.dota2items.util;

public class Dump {
	/**
	 * When recursively removing a tree, the algorithm will only traverse
	 * leaves this far from the trunk in the main direction:
	 */
	public static final float maxFoliageRadiusMain = 4;

	/*private void removeTree(World world, IntVec3 trunkBase) {
		// Brace yourselves, this is a recursive method!
		recursiveRemoveTree(world, trunkBase.x, trunkBase.y, trunkBase.z, 0, SideHit.TOP);
	}
	
	private void recursiveRemoveTree(World world, int x, int y, int z,
			float distanceFromWood, int direction) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null) {
			return;
		} else if (block == Block.wood) {
			// Clear this block
			world.setBlock(x, y, z, 0);
			// Reset distance counter
			distanceFromWood = 0;
		} else if (block == Block.leaves) {
			// Clear this block
			world.setBlock(x, y, z, 0);
			// Getting farther and farther from wood...
			distanceFromWood++;
			// ... but not too far
			if (distanceFromWood > maxFoliageRadiusMain)
				return;
		} else if (
				// Clear blocks like cocoa plants and snow caps
				block == Block.snow ||
				block == Block.cocoaPlant ||
				block instanceof BlockFlower // flowers and mushrooms
				){
			world.setBlock(x, y, z, 0);
			return;
		} else if (block == Block.vine) {
			// Vines need to be taken down to the bottom
			int vineY = y;
			while (Block.blocksList[world.getBlockId(x, vineY, z)] == Block.vine) {
				world.setBlock(x, vineY, z, 0);
				vineY--;
			}
		}
		else {
			return;
		}
		// First continue looking in the direction we were facing
		int destX = x;
		int destY = y;
		int destZ = z;
		switch(direction) {
		case SideHit.NORTH:
			destX++;
			break;
		case SideHit.SOUTH:
			destX--;
			break;
		case SideHit.TOP:
			destY++;
			break;
		case SideHit.BOTTOM:
			destY--;
			break;
		case SideHit.EAST:
			destZ++;
			break;
		case SideHit.WEST:
			destZ--;
			break;
		}
		recursiveRemoveTree(world, destX, destY, destZ, distanceFromWood, direction);
		// Then go in the other 5 directions
		if (direction != SideHit.NORTH) recursiveRemoveTree(world, x+1, y, z, distanceFromWood, SideHit.NORTH);
		if (direction != SideHit.SOUTH) recursiveRemoveTree(world, x-1, y, z, distanceFromWood, SideHit.SOUTH);
		if (direction != SideHit.TOP) recursiveRemoveTree(world, x, y+1, z, distanceFromWood, SideHit.TOP);
		//if (direction != SideHit.BOTTOM) recursiveRemoveTree(world, x, y-1, z, distanceFromWood, SideHit.BOTTOM);
		if (direction != SideHit.EAST) recursiveRemoveTree(world, x, y, z+1, distanceFromWood, SideHit.EAST);
		if (direction != SideHit.WEST) recursiveRemoveTree(world, x, y, z-1, distanceFromWood, SideHit.WEST);
	}*/
	
	
	
	
	
	
	
	
	
	/*private void removeTree(World world, IntVec3 trunkBase) {
		// Brace yourselves, this is a recursive method!
		recursiveRemoveTree(world, trunkBase.x, trunkBase.y, trunkBase.z, 0, SideHit.TOP, 0);
	}
	
	private void recursiveRemoveTree(World world, int x, int y, int z,
			float distanceFromWood, int direction, int branchOrder) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null) {
			return;
		} else if (block == Block.wood) {
			// Clear this block
			world.setBlock(x, y, z, 0);
			// Reset distance counter
			if (distanceFromWood > 0)
				distanceFromWood = 1;
			branchOrder = 0;
		} else if (block == Block.leaves) {
			// Clear this block
			world.setBlock(x, y, z, 0);
			if (branchOrder > 0) {
				// Getting farther and farther from wood...
				distanceFromWood++;
				// ... but not too far
				if (distanceFromWood > maxFoliageRadiusMain)
					return;
			}
		} else if (
				// Clear blocks like cocoa plants and snow caps
				block == Block.snow ||
				block == Block.cocoaPlant ||
				block instanceof BlockFlower // flowers and mushrooms
				){
			world.setBlock(x, y, z, 0);
			return;
		} else if (block == Block.vine) {
			// Vines need to be taken down to the bottom
			int vineY = y;
			while (Block.blocksList[world.getBlockId(x, vineY, z)] == Block.vine) {
				world.setBlock(x, vineY, z, 0);
				vineY--;
			}
		}
		else {
			return;
		}
		// Go look in all 6 directions
		continueRecursiveRemoveTree(world, x-1, y, z, distanceFromWood, direction, SideHit.NORTH, branchOrder);
		continueRecursiveRemoveTree(world, x+1, y, z, distanceFromWood, direction, SideHit.SOUTH, branchOrder);
		continueRecursiveRemoveTree(world, x, y, z-1, distanceFromWood, direction, SideHit.EAST, branchOrder);
		continueRecursiveRemoveTree(world, x, y, z+1, distanceFromWood, direction, SideHit.WEST, branchOrder);
		continueRecursiveRemoveTree(world, x, y+1, z, distanceFromWood, direction, SideHit.TOP, branchOrder);
		continueRecursiveRemoveTree(world, x, y-1, z, distanceFromWood, direction, SideHit.BOTTOM, branchOrder);
	}
	
	private void continueRecursiveRemoveTree(World world, int x, int y, int z,
				float distanceFromWood, int oldDirection, int newDirection, int branchOrder) {
			if (oldDirection != newDirection) branchOrder++;
			recursiveRemoveTree(world, x, y, z, distanceFromWood, newDirection, branchOrder);
		}*/
		
		/*private void removeTree(World world, IntVec3 trunkBase) {
		// Brace yourselves, this is a recursive method!
		int x = trunkBase.x;		
		int y = trunkBase.y;
		int z = trunkBase.z;
		while (y < world.getHeight()) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if (block != Block.wood && block != Block.leaves && block != Block.snow)
				break;
			else
				world.setBlock(x, y, z, 0);
			y++;
		}
		removeAllFlora(world, new IntVec3(x - treeHalfSide, trunkBase.y, z - treeHalfSide),
				new IntVec3(x + treeHalfSide, y, z + treeHalfSide));
	}
	
	*//** Removes all flora within the box defined by opposite vertices p1 and p2. *//*
	private void removeAllFlora(World world, IntVec3 p1, IntVec3 p2) {
		for (int y = p1.y; y <= p2.y; y++) {
			for (int x = p1.x; x <= p2.x; x++) {
				for (int z = p1.z; z <= p2.z; z++) {
					Block block = Block.blocksList[world.getBlockId(x, y, z)];
					if (block == null) {
						continue;
					} else if (block == Block.wood) {
						removeTree(world, new IntVec3(x, y, z));
					} else if (block == Block.leaves) {
						 removeVine(world, x-1, y, z);
						 removeVine(world, x+1, y, z);
						 removeVine(world, x, y, z-1);
						 removeVine(world, x, y, z+1);
						 world.setBlock(x, y, z, 0);
					} else if (block == Block.vine) {
						removeVine(world, x, y, z);
					} else if (
							block == Block.snow ||
							block == Block.cocoaPlant || 
							block instanceof BlockFlower) {
						world.setBlock(x, y, z, 0);
					}
				}
			}
		}
	}*/
}
