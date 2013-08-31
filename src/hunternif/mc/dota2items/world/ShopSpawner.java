package hunternif.mc.dota2items.world;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.util.BlockUtil;
import hunternif.mc.dota2items.util.IntVec3;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFactory;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.product.PasteToWorld;

public class ShopSpawner {
	private static final String KEY_SHOP_SAVE_DATA = "D2IShopData";

	private SchematicFile shopSchematic;
	public void init() {
		URL shopSchematicUrl = Dota2Items.class.getResource("/assets/dota2items/models/shop.schematic");
		try {
			shopSchematic = new SchematicFile(shopSchematicUrl.openStream());
		} catch (IOException e) {
			Dota2Items.logger.severe("Failed to load shop.schematic: " + e.toString());
		}
	}
	
	private ShopSavedData shopData;
	
	@ForgeSubscribe
	public void onPopulateChunk(PopulateChunkEvent.Post event) {
		if (!event.world.isRemote && shopData != null && event.world.villageCollectionObj != null) {
			List<ChunkCoordinates> villageCoords = new ArrayList<ChunkCoordinates>();
			List<Village> villages = event.world.villageCollectionObj.getVillageList();
			for (Village village : villages) {
				ChunkCoordinates coords = village.getCenter();
				if (isVillageVisited(coords)) {
					continue;
				}
				spawnShop(event.world, coords.posX, coords.posZ);
				shopData.visitedVillageCoords.add(village.getCenter());
				shopData.setDirty(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote) {
			shopData = (ShopSavedData) event.world.loadItemData(ShopSavedData.class, KEY_SHOP_SAVE_DATA);
			if (shopData == null) {
				shopData = new ShopSavedData(KEY_SHOP_SAVE_DATA);
				shopData.setDirty(true);
				event.world.setItemData(KEY_SHOP_SAVE_DATA, shopData);
			}
		}
	}
	
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event) {
		if (!event.world.isRemote) {
			event.world.setItemData(KEY_SHOP_SAVE_DATA, shopData);
		}
	}
	
	private void spawnShop(World world, int x, int z) {
		PasteToWorld pasteToWorld = new PasteToWorld(world);
		IntVec3 coords = BlockUtil.findSurfaceDownward(world, x - 40 - shopSchematic.getWidth(), world.getHeight(), z - shopSchematic.getLength());
		if (coords == null) {
			Dota2Items.logger.warning("Couldn't find surface to spawn shop at (x:" + x + ", z:" + z + ")");
		} else {
			pasteToWorld.setWorldVec(new SchVector(coords.x, coords.y, coords.z));
			SchematicFactory factory = new SchematicFactory().loadSchematic(shopSchematic);
			factory.produce(pasteToWorld);
			EntityShopkeeper shopkeeper = new EntityShopkeeper(world);
			shopkeeper.setLocationAndAngles(2.5 + coords.x, coords.y, 6.5 + coords.z, 0, 0);
			world.spawnEntityInWorld(shopkeeper);
			Dota2Items.logger.info("Spawned shop at (" + coords.x + ", " + coords.y + ", " + coords.z + ")");
		}
	}
	
	private static final int VILLAGE_DISTANCE = 64;
	/** Villages centers sometimes change coordinates for no reason,
	 * so gotta approximate the match.
	 */
	private boolean isVillageVisited(ChunkCoordinates coords) {
		for (ChunkCoordinates visitedCoords : shopData.visitedVillageCoords) {
			int dx = Math.abs(visitedCoords.posX - coords.posX);
			int dy = Math.abs(visitedCoords.posY - coords.posY);
			int dz = Math.abs(visitedCoords.posZ - coords.posZ);
			if (dx < VILLAGE_DISTANCE && dy < VILLAGE_DISTANCE && dz < VILLAGE_DISTANCE) {
				return true;
			}
		}
		return false;
	}
}
