package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.relauncher.Side;

/**
 * Used for to keep track of Dota 2 Items in player's inventory for 2 purposes:
 * <ul>
 * <li>Retain the items upon death.</li>
 * <li>Keep the passive buffs up-to-date.</li>
 * </ul>
 */
public class ItemTracker implements IPlayerTracker, IEntityUpdater {
	/** Hold the Dota 2 items that the player had when he died. */
	private Map<EntityPlayer, List<ItemStack>> retainedItems = new ConcurrentHashMap<EntityPlayer, List<ItemStack>>();
	
	/** Maps containing players' inventory hotbars from previous ticks. Used to
	 * keep the passive buffs up-to-date. */
	private Map<EntityPlayer, ItemStack[]> clientInventories = new ConcurrentHashMap<EntityPlayer, ItemStack[]>(),
										   serverInventories = new ConcurrentHashMap<EntityPlayer, ItemStack[]>();
	private Map<EntityPlayer, ItemStack[]> getInventoryMap(Side side) {
		return side.isClient() ? clientInventories : serverInventories;
	}
	
	private static Side getSide(Entity entity) {
		return entity.worldObj.isRemote ? Side.CLIENT : Side.SERVER;
	}
	
	@ForgeSubscribe
	public void onPlayerDrops(PlayerDropsEvent event) {
		if (event.entity.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			// If "keepInventory" is turned on, the items will return to you anyway.
			return;
		}
		// Remember all the Dota 2 items the player had in inventory.
		// In case he logs out while dead, put them back into his inventory for storage.
		Iterator<EntityItem> iter = event.drops.iterator();
		List<ItemStack> list = retainedItems.get(event.entityPlayer);
		if (list == null) {
			list = new ArrayList<ItemStack>();
			retainedItems.put(event.entityPlayer, list);
		}
		while (iter.hasNext()) {
			EntityItem entityItem = iter.next();
			ItemStack stack = entityItem.getEntityItem();
			if (stack.getItem() instanceof Dota2Item) {
				Dota2Item dota2Item = (Dota2Item) stack.getItem();
				if (!dota2Item.getDropsOnDeath()) {
					iter.remove();
					list.add(stack.copy());
					event.entityPlayer.inventory.addItemStackToInventory(stack);
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onUseDota2Item(UseItemEvent event) {
		if (event.isCanceled()) return;
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(event.entityPlayer);
		for (BuffInstance buffInst : stats.getAppliedBuffs()) {
			if (buffInst.buff.isRemovedOnAction) {
				stats.removeBuff(buffInst);
			}
		}
	}
	
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		// If the player logged in while he was dead, need to save his Dota 2
		// items, because they will be subsequently removed on respawn.
		if (player.getHealth() <= 0 && !player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
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
	public void onPlayerLogout(EntityPlayer player) {
		// When this player logs back in, his entityID will be different and it
		// won't equal the one stored in the map. Besides, his items are stored
		// back in his inventory.
		// By doing this we prevent a memory leak:
		retainedItems.remove(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		if (!player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			List<ItemStack> list = retainedItems.get(player);
			if (list != null) {
				for (ItemStack stack : list) {
					player.inventory.addItemStackToInventory(stack);
				}
			}
		}
		retainedItems.remove(player);
		updatePlayerInventoryBuffs(player);
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
		stats.setMana(stats.getMaxMana());
		stats.sendSyncPacketToClient(player);
	}
	
	/** Checks if the items in the player's inventory are the same as last time.
	 * If not, calls {@link #updatePlayerInventoryBuffs(EntityPlayer)}. */
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
	
	/** Removes all item-induced passive buffs and add the buffs from the items
	 * currently in the inventory. */
	private void updatePlayerInventoryBuffs(EntityPlayer player) {
		if (Dota2Items.debug) {
			Dota2Items.logger.info("Updating buffs on player " + player.username);
		}
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
					stats.addBuff(new BuffInstance(item.getPassiveBuff(), player, true));
				}
			}
		}
		// Add the item being dragged too:
		ItemStack stack = player.inventory.getItemStack();
		if (stack != null && stack.getItem() instanceof Dota2Item) {
			Dota2Item item = (Dota2Item) stack.getItem();
			if (item.getPassiveBuff() != null) {
				stats.addBuff(new BuffInstance(item.getPassiveBuff(), player, true));
			}
		}
	}
	
	/** Returns true if both hotbars contain the same items. */
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

	@Override
	public void update(EntityLivingBase entity, EntityStats stats, LivingUpdateEvent event) {
		if (entity instanceof EntityPlayer) {
			checkAndUpdatePlayerInventory((EntityPlayer)entity);
		}
	}
}
