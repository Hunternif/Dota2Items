package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.network.BuffPacket;
import hunternif.mc.dota2items.network.EntityStatsSyncPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/** Used to retain Dota 2 Items on death. */
public class Dota2PlayerTracker implements IPlayerTracker {
	public Map<EntityPlayer, List<ItemStack>> retainedItems = new ConcurrentHashMap<EntityPlayer, List<ItemStack>>();
	
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
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		for (BuffInstance buffInst : stats.getAppliedBuffs()) {
			if (!buffInst.isItemPassiveBuff) {
				PacketDispatcher.sendPacketToAllPlayers(new BuffPacket(buffInst).makePacket());
			}
		}
		PacketDispatcher.sendPacketToPlayer(new EntityStatsSyncPacket(stats).makePacket(), (Player)player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		List<ItemStack> list = retainedItems.get(player);
		if (list != null) {
			for (ItemStack stack : list) {
				player.inventory.addItemStackToInventory(stack);
			}
			retainedItems.remove(player);
		}
		EntityStats stats = Dota2Items.mechanics.onPlayerRespawn(player);
		PacketDispatcher.sendPacketToPlayer(new EntityStatsSyncPacket(stats).makePacket(), (Player)player);
		
		//TODO: bug: sometimes on respawn mana is not sync'ed immediately.
	}

}
