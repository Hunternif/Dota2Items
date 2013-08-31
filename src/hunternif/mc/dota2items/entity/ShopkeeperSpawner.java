package hunternif.mc.dota2items.entity;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.util.BlockUtil;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.MCConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class ShopkeeperSpawner {
	private static final float UPDATE_INTERVAL = 5;
	private Set<ChunkCoordinates> shopkeepersSpawned = new HashSet<ChunkCoordinates>();
	
	public void updateVillages(World world) {
		if (!world.isRemote && world.getTotalWorldTime() % (long)(MCConstants.TICKS_PER_SECOND * UPDATE_INTERVAL) == 0) {
			List<Village> villages = world.villageCollectionObj.getVillageList();
			for (Village village : villages) {
				if (!shopkeepersSpawned.contains(village.getCenter())) {
					shopkeepersSpawned.add(village.getCenter());
					int centerX = village.getCenter().posX;
					int centerY = village.getCenter().posY;
					int centerZ = village.getCenter().posZ;
					IntVec3 spawn = BlockUtil.findSurface(world, centerX, centerY, centerZ);
					if (spawn != null) {
						List <Entity> shopkeepersFound = shopkeepersAround(world, spawn);
						if (shopkeepersFound.isEmpty()) {
							EntityShopkeeper shopkeeper = new EntityShopkeeper(world);
							shopkeeper.setLocationAndAngles(spawn.x, spawn.y, spawn.z, 0, 0);
							world.spawnEntityInWorld(shopkeeper);
							Dota2Items.logger.info(String.format("Spawned a Shopkeeper at (%d, %d, %d)", spawn.x, spawn.y, spawn.z));
						} else if (shopkeepersFound.size() > 1) {
							// Delete redundant shopkeepers:
							for (int i = 1; i < shopkeepersFound.size(); i++) {
								world.removeEntity(shopkeepersFound.get(i));
							}
						}
					}
					if (spawn == null) {
						Dota2Items.logger.warning(String.format("Couldn't find space to spawn a Shopkeeper at (%d, %d, %d)", centerX, centerY, centerZ));
					}
				}
			}
		}
	}
	
	public static List<Entity> shopkeepersAround(World world, IntVec3 vec) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(vec.x, vec.y, vec.z, vec.x+1, vec.y+1, vec.z+1);
		box = box.expand(80, 80, 80);
		return world.getEntitiesWithinAABB(EntityShopkeeper.class, box);
	}
}
