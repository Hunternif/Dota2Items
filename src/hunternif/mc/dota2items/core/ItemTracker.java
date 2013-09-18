package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.relauncher.Side;

/** Used to retain Dota 2 Items on death. */
public class ItemTracker implements IPlayerTracker {
	private Map<EntityPlayer, List<ItemStack>> retainedItems = new ConcurrentHashMap<EntityPlayer, List<ItemStack>>();
	
	private Map<EntityPlayer, ItemStack[]> clientInventories = new ConcurrentHashMap<EntityPlayer, ItemStack[]>();
	private Map<EntityPlayer, ItemStack[]> serverInventories = new ConcurrentHashMap<EntityPlayer, ItemStack[]>();
	private Map<EntityPlayer, ItemStack[]> getInventoryMap(Side side) {
		return side.isClient() ? clientInventories : serverInventories;
	}
	
	private static Side getSide(Entity entity) {
		return entity.worldObj.isRemote ? Side.CLIENT : Side.SERVER;
	}
	
	@ForgeSubscribe
	public void onPlayerDrops(PlayerDropsEvent event) {
		Iterator<EntityItem> iter = event.drops.iterator();
		while (iter.hasNext()) {
			EntityItem entityItem = iter.next();
			ItemStack stack = entityItem.getEntityItem();
			if (stack.getItem() instanceof Dota2Item) {
				Dota2Item dota2Item = (Dota2Item) stack.getItem();
				if (!dota2Item.getDropsOnDeath()) {
					iter.remove();
					List<ItemStack> list = retainedItems.get(event.entityPlayer);
					if (list == null) {
						list = new ArrayList<ItemStack>();
						retainedItems.put(event.entityPlayer, list);
					}
					list.add(stack.copy());
					event.entityPlayer.inventory.addItemStackToInventory(stack);
				}
			}
		}
	}
	
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if (player.func_110143_aJ() <= 0) {
			List<ItemStack> list = retainedItems.get(player);
			if (list == null) {
				list = new ArrayList<ItemStack>();
				retainedItems.put(player, list);
			}
			for (ItemStack stack : player.inventory.mainInventory) {
				if (stack != null) {
					list.add(stack.copy());
				}
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		List<ItemStack> list = retainedItems.get(player);
		if (list != null) {
			for (ItemStack stack : list) {
				player.inventory.addItemStackToInventory(stack);
			}
			retainedItems.remove(player);
		}
		updatePlayerInventoryBuffs(player);
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		stats.setMana(stats.getMaxMana());
		stats.sendSyncPacketToClient(player);
	}
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		EntityStats stats = Dota2Items.stats.getEntityStats(event.entityLiving);
		if (stats != null && event.entityLiving instanceof EntityPlayer) {
			checkAndUpdatePlayerInventory((EntityPlayer)event.entityLiving);
		}
	}
	
	private void checkAndUpdatePlayerInventory(EntityPlayer player) {
		Side side = getSide(player);
		Map<EntityPlayer, ItemStack[]> inventoryMap = getInventoryMap(side);
		if (player.inventory == null) {
			return;
		}
		ItemStack[] currentInventory = Arrays.copyOfRange(player.inventory.mainInventory, 0, 10);
		// Check the item being dragged too:
		currentInventory[9] = player.inventory.getItemStack();
		ItemStack[] oldInventory = inventoryMap.get(player);
		if (oldInventory == null) {
			inventoryMap.put(player, currentInventory);
			updatePlayerInventoryBuffs(player);
		} else {
			if (!isHotbarWithSameItems(currentInventory, oldInventory)) {
				inventoryMap.put(player, currentInventory);
				updatePlayerInventoryBuffs(player);
			}
		}
	}
	
	private void updatePlayerInventoryBuffs(EntityPlayer player) {
		Dota2Items.logger.fine("Updating buffs on player " + player.username);
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		// Remove all passive item Buffs to add them again later:
		for (BuffInstance buffInst : stats.getAppliedBuffs()) {
			if (buffInst.isItemPassiveBuff()) {
				stats.removeBuff(buffInst);
			}
		}
		for (int i = 0; i < 10; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if (stack != null && stack.getItem() instanceof Dota2Item) {
				Dota2Item item = (Dota2Item) stack.getItem();
				if (item.getPassiveBuff() != null) {
					stats.addBuff(new BuffInstance(item.getPassiveBuff(), player.entityId, true));
				}
			}
		}
		// Add the item being dragged too:
		ItemStack stack = player.inventory.getItemStack();
		if (stack != null && stack.getItem() instanceof Dota2Item) {
			Dota2Item item = (Dota2Item) stack.getItem();
			if (item.getPassiveBuff() != null) {
				stats.addBuff(new BuffInstance(item.getPassiveBuff(), player.entityId, true));
			}
		}
	}
	
	private static boolean isHotbarWithSameItems(ItemStack[] bar1, ItemStack[] bar2) {
		if (bar1.length != bar2.length) {
			return false;
		}
		for (int i = 0; i < bar1.length; i++) {
			ItemStack stack1 = bar1[i];
			ItemStack stack2 = bar2[i];
			if ((stack1 == null && stack2 != null) ||
				(stack1 != null && stack2 == null) ||
				(stack1 != null && stack2 != null && !stack1.isItemEqual(stack2))) {
				return false;
			}
		}
		return true;
	}
}
