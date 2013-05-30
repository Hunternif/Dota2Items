package hunternif.mc.dota2items.mechanics;

import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.mechanics.buff.Dota2EntityBuff;
import hunternif.mc.dota2items.mechanics.inventory.Dota2PlayerTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class Dota2ItemMechanics implements IUpdatable {
	
	private List<Dota2EntityBuff> buffs = Collections.synchronizedList(new ArrayList<Dota2EntityBuff>());
	
	public Dota2PlayerTracker playerTracker = new Dota2PlayerTracker();
	
	private Map<Integer /*entityID*/, Dota2EntityStats> clientEntityStats = new ConcurrentHashMap<Integer, Dota2EntityStats>();
	private Map<Integer /*entityID*/, Dota2EntityStats> serverEntityStats = new ConcurrentHashMap<Integer, Dota2EntityStats>();
	
	//TODO int, str, agi
	
	@ForgeSubscribe
	public void onPlayerDrops(PlayerDropsEvent event) {
		Iterator<EntityItem> iter = event.drops.iterator();
		while (iter.hasNext()) {
			EntityItem entityItem = iter.next();
			ItemStack stack = entityItem.getEntityItem();
			if (stack.getItem() instanceof Dota2Item) {
				Dota2Item dota2Item = (Dota2Item) stack.getItem();
				if (!dota2Item.dropsOnDeath()) {
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
			Dota2EntityStats stats = getEntityStats(entity);
			if (stats != null && !stats.canAttack()) {
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onLivingHurt(LivingHurtEvent event) {
		float damage = event.ammount;
		
		// Check if the target entity is invulnerable
		Dota2EntityStats targetStats = getEntityStats(event.entityLiving);
		if (targetStats != null && targetStats.isInvulnerable()) {
			event.setCanceled(true);
			return;
		}
		
		// Apply attack bonuses to the source player
		if (event.source.getEntity() instanceof EntityPlayer) {
			//System.out.println("Base damage: " + event.ammount);
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			List<Dota2Item> directBonus = new ArrayList<Dota2Item>();
			List<Dota2Item> percentBonus = new ArrayList<Dota2Item>();
			for (int i = 0; i < 10; i++) {
				ItemStack stack = player.inventory.mainInventory[i];
				if (stack != null && stack.getItem() instanceof Dota2Item) {
					Dota2Item dota2Item = (Dota2Item) stack.getItem();
					if (dota2Item.hasDirectAttackBonus())
						directBonus.add(dota2Item);
					if (dota2Item.hasPercentAttackBonus())
						percentBonus.add(dota2Item);
				}
			}
			for (Dota2Item item : directBonus) {
				damage = item.applyDirectAttackBonus(damage, event.source.isProjectile());
			}
			for (Dota2Item item : percentBonus) {
				damage = item.applyPercentAttackBonus(damage, event.source.isProjectile());
			}
			event.ammount = MathHelper.floor_float(damage);
			//System.out.println("Amplified damage: " + event.ammount);
		}
		// Apply armor bonuses to the player being hurt
		if (event.entityLiving instanceof EntityPlayer) {
			float baseArmor = 0;
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			List<Dota2Item> directBonus = new ArrayList<Dota2Item>();
			List<Dota2Item> percentBonus = new ArrayList<Dota2Item>();
			for (int i = 0; i < 10; i++) {
				ItemStack stack = player.inventory.mainInventory[i];
				//NOTE: will have to pass itemStack instead for the Bloodstone.
				if (stack != null && stack.getItem() instanceof Dota2Item) {
					Dota2Item dota2Item = (Dota2Item) stack.getItem();
					if (dota2Item.hasDirectArmorBonus())
						directBonus.add(dota2Item);
					if (dota2Item.hasPercentArmorBonus())
						percentBonus.add(dota2Item);
				}
			}
			for (Dota2Item item : directBonus) {
				baseArmor = item.applyDirectArmorBonus(baseArmor);
			}
			for (Dota2Item item : percentBonus) {
				baseArmor = item.applyPercentArmorBonus(baseArmor);
			}
			// The formula was taken from Dota 2 Wiki
			float damageMultiplier = 1f;
			if (baseArmor > 0) {
				damageMultiplier = 1f - ((0.06f * baseArmor) / (1 + 0.06f * baseArmor));
			} else if (baseArmor < 0) {
				baseArmor = Math.max(-20f, baseArmor);
				damageMultiplier = 2f - (float) Math.pow(0.94, (double) -baseArmor);
			}
			damage *= damageMultiplier;
			event.ammount = MathHelper.floor_float(damage);
		}
	}
	
	public void onUpdate(boolean isRemote) {
		// Update buffs
		synchronized (buffs) {
			Iterator<Dota2EntityBuff> iter = buffs.iterator();
			while (iter.hasNext()) {
				Dota2EntityBuff buff = iter.next();
				if (buff.stats.entity.worldObj.getTotalWorldTime() >= buff.endTime) {
					buff.setEndStats();
					iter.remove();
				}
			}
		}
	}
	
	public void addBuff(Dota2EntityBuff buff) {
		buff.setStartStats();
		buffs.add(buff);
	}
	
	public void putEntityStats(Dota2EntityStats stats) {
		if (stats.entity.worldObj.isRemote) {
			clientEntityStats.put(stats.entity.entityId, stats);
		} else {
			serverEntityStats.put(stats.entity.entityId, stats);
		}
	}
	public Dota2EntityStats getEntityStats(Entity entity) {
		if (entity == null) return null;
		if (entity.worldObj.isRemote) {
			return getEntityStatsClient(entity.entityId);
		} else {
			return getEntityStatsServer(entity.entityId);
		}
	}
	public Dota2EntityStats getEntityStatsClient(int entityID) {
		return clientEntityStats.get(entityID);
	}
	public Dota2EntityStats getEntityStatsServer(int entityID) {
		return serverEntityStats.get(entityID);
	}
}
