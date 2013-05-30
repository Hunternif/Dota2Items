package hunternif.mc.dota2items.mechanics.inventory;

import hunternif.mc.dota2items.item.Dota2Item;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class Dota2InventoryTracker implements ITickHandler {
	public Map<Integer /*entityID*/, Set<Dota2Item>> dota2Inventory = new ConcurrentHashMap<Integer, Set<Dota2Item>>();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.PLAYER)) {
			EntityPlayer player = (EntityPlayer) tickData[0];
			Set<Dota2Item> currentItems = new HashSet<Dota2Item>();
			for (int i = 0; i < 10; i++) {
				//NOTE: items like BloodStone will need their charges
				Item item = player.inventory.mainInventory[i].getItem();
				if (item instanceof Dota2Item) currentItems.add((Dota2Item) item);
			}
			Set<Dota2Item> prevItems = dota2Inventory.get(player.entityId);
			if (prevItems == null || !currentItems.equals(prevItems)) {
				dota2Inventory.put(player.entityId, currentItems);
				//TODO report item buffs
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "Dota 2 Inventory Tick Handler";
	}
}
