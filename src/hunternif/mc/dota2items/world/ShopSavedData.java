package hunternif.mc.dota2items.world;

import hunternif.mc.dota2items.Dota2Items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldSavedData;

public class ShopSavedData extends WorldSavedData {
	private static final String TAG_VILLAGE_LIST = "D2IWorldVillageList";
	private static final String TAG_VILLAGE_COORDS = "D2IWorldVillageCoords";
	
	public Set<ChunkCoordinates> visitedVillageCoords = new HashSet<ChunkCoordinates>();
	
	public ShopSavedData(String key) {
		super(key);
	}

	@Override
	public void readFromNBT(NBTTagCompound worldTag) {
		NBTTagList villagesTag = worldTag.getTagList(TAG_VILLAGE_LIST);
		for (int i = 0; i < villagesTag.tagCount(); i++) {
			NBTTagCompound coordsTag = (NBTTagCompound) villagesTag.tagAt(i);
			int[] coords = coordsTag.getIntArray(TAG_VILLAGE_COORDS);
			visitedVillageCoords.add(new ChunkCoordinates(coords[0], coords[1], coords[2]));
			if (Dota2Items.debug) {
				Dota2Items.logger.info("Found shop at (" + coords[0] + ", " + coords[2] + ")");
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound worldTag) {
		NBTTagList villagesTag = worldTag.getTagList(TAG_VILLAGE_LIST);
		for (ChunkCoordinates coords : visitedVillageCoords) {
			NBTTagCompound coordsTag = new NBTTagCompound();
			coordsTag.setIntArray(TAG_VILLAGE_COORDS, new int[]{coords.posX, coords.posY, coords.posZ});
			villagesTag.appendTag(coordsTag);
		}
		worldTag.setTag(TAG_VILLAGE_LIST, villagesTag);
	}

}
