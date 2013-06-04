package hunternif.mc.dota2items.core.inventory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IPlayerTracker;

/** Used to retain Dota 2 Items on death. */
public class Dota2PlayerTracker implements IPlayerTracker {
	public Map<Integer, List<ItemStack>> retainedItems = new ConcurrentHashMap<Integer, List<ItemStack>>();
	
	@Override
	public void onPlayerLogin(EntityPlayer player) {
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		List<ItemStack> list = retainedItems.get(Integer.valueOf(player.entityId));
		if (list != null) {
			for (ItemStack stack : list) {
				player.inventory.addItemStackToInventory(stack);
			}
			retainedItems.remove(Integer.valueOf(player.entityId));
		}
	}

}
