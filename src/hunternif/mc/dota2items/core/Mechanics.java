package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.core.inventory.Dota2PlayerTracker;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class Mechanics {
	private static String[] walkSpeedObfFields = {"g", "field_75097_g", "walkSpeed"};
	
	public Dota2PlayerTracker playerTracker = new Dota2PlayerTracker();
	
	public Map<Entity, EntityStats> entityStats = new ConcurrentHashMap<Entity, EntityStats>();
	public Map<EntityPlayer, ItemStack[]> inventories = new ConcurrentHashMap<EntityPlayer, ItemStack[]>();
	
	@ForgeSubscribe
	public void onPlayerDrops(PlayerDropsEvent event) {
		Iterator<EntityItem> iter = event.drops.iterator();
		while (iter.hasNext()) {
			EntityItem entityItem = iter.next();
			ItemStack stack = entityItem.getEntityItem();
			if (stack.getItem() instanceof Dota2Item) {
				Dota2Item dota2Item = (Dota2Item) stack.getItem();
				if (!dota2Item.dropsOnDeath) {
					iter.remove();
					List<ItemStack> list = playerTracker.retainedItems.get(Integer.valueOf(event.entityPlayer.entityId));
					if (list == null) {
						list = new ArrayList<ItemStack>();
					}
					list.add(stack.copy());
					playerTracker.retainedItems.put(Integer.valueOf(event.entityPlayer.entityId), list);
					event.entityPlayer.inventory.addItemStackToInventory(stack);
					//TODO write dota 2 inventory in some NBT file, because
					// it is lost if the player logs out while dead.
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onLivingAttack(LivingAttackEvent event) {
		// Check if the entity can attack
		//TODO this doesn't work for creepers because they don't actually attack
		Entity entity = event.source.getEntity();
		if (entity != null) {
			EntityStats stats = entityStats.get(entity);
			if (stats != null && !stats.canAttack()) {
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onLivingHurt(LivingHurtEvent event) {
		int damage = event.ammount;
		
		// Check if the target entity is invulnerable
		EntityStats targetStats = entityStats.get(event.entityLiving);
		if (targetStats != null && targetStats.isInvulnerable()) {
			System.out.println("invulnerable");
			event.setCanceled(true);
			return;
		}
		
		// Apply attack bonuses to the source player
		if (event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			EntityStats sourceStats = entityStats.get(player);
			if (sourceStats != null) {
				//TODO Quelling blade doesn't stack. Also you can't have more than 1 in inventory
				damage = sourceStats.getDamage(damage, !event.source.isProjectile());
			}
		}
		
		// Apply armor bonuses to the player being hurt
		int armor = 0;
		if (targetStats != null) {
			armor = targetStats.getArmor(armor);
		}
		
		// The formula was taken from Dota 2 Wiki
		float damageMultiplier = 1f;
		if (armor > 0) {
			damageMultiplier = 1f - ((0.06f * armor) / (1 + 0.06f * armor));
		} else if (armor < 0) {
			armor = Math.max(-20, armor);
			damageMultiplier = 2f - (float) Math.pow(0.94, (double) -armor);
		}
		float damage_float = (float)damage * damageMultiplier;
		event.ammount = MathHelper.floor_float(damage_float);
	}
	
	public void updateAllEntityStats() {
		for (Entry<Entity, EntityStats> entry : entityStats.entrySet()) {
			Entity entity = entry.getKey();
			EntityStats stats = entry.getValue();
			for (BuffInstance buffInst : stats.getAppliedBuffs()) {
				if (!buffInst.isItemPassiveBuff && entity.worldObj.getTotalWorldTime() > buffInst.endTime) {
					stats.removeBuff(buffInst);
				}
			}
		}
	}
	
	public void updatePlayerInventories(boolean isRemote) {
		List<EntityPlayer> players;
		if (isRemote) {
			players = new ArrayList<EntityPlayer>();
			if (Minecraft.getMinecraft().thePlayer != null) {
				players.add(Minecraft.getMinecraft().thePlayer);
			}
		} else {
			players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		}
		//TODO check multiplayer to work properly here
		for (EntityPlayer player : players) {
			if (player.inventory == null) {
				continue;
			}
			ItemStack[] currentInventory = Arrays.copyOfRange(player.inventory.mainInventory, 0, 10);
			ItemStack[] oldInventory = inventories.get(player);
			if (oldInventory == null) {
				inventories.put(player, currentInventory);
				updatePlayerBuffs(player, currentInventory);
			} else {
				if (!sameItemsStacks(currentInventory, oldInventory)) {
					inventories.put(player, currentInventory);
					updatePlayerBuffs(player, currentInventory);
				}
			}
		}
	}
	
	private void updatePlayerBuffs(EntityPlayer player, ItemStack[] inventory) {
		System.out.println("updating buffs on player");
		EntityStats stats = entityStats.get(player);
		if (stats == null) {
			stats = new EntityStats();
			entityStats.put(player, stats);
		} else {
			// Remove all passive item Buffs to add them again later
			for (BuffInstance buffInst : stats.getAppliedBuffs()) {
				if (buffInst.isItemPassiveBuff) {
					stats.removeBuff(buffInst);
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			ItemStack stack = player.inventory.mainInventory[i];
			if (stack != null && stack.getItem() instanceof Dota2Item) {
				Dota2Item item = (Dota2Item) stack.getItem();
				if (item.passiveBuff != null) {
					//stats.appliedBuffs.add(new BuffInstance(item.passiveBuff, player.entityId, true));
					stats.addBuff(new BuffInstance(item.passiveBuff, player.entityId, true));
				}
			}
		}
		//NOTE for now movement speed bonus will only be applied to players, not to mobs
		ReflectionHelper.setPrivateValue(PlayerCapabilities.class, player.capabilities, stats.getMovementSpeed(), walkSpeedObfFields);
	}
	
	private static boolean sameItemsStacks(ItemStack[] bar1, ItemStack[] bar2) {
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
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		EntityStats stats = entityStats.get(event.entity);
		if (stats != null && !stats.canMove()) {
			event.setCanceled(true);
			if (event.entity instanceof EntityPlayer) {
				((EntityPlayer)event.entity).inventory.decrementAnimations();
			}
		}
	}
}
