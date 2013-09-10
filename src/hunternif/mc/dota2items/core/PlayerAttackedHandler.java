package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.item.BlinkDagger;
import hunternif.mc.dota2items.network.PlayerHurtPacket;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PlayerAttackedHandler {
	@ForgeSubscribe
	public void onHurt(LivingHurtEvent event) {
		// Why is this only called on the server?
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			
			// If the player is attacked by living entities, some items start cooldown:
			if (event.source.getEntity() instanceof EntityLivingBase) {
				int invSize = player.inventory.getSizeInventory();
				for (int i = 0; i < invSize; i++) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					// Blink Dagger
					if (stack != null && stack.itemID == Config.blinkDagger.getID()) {
						float cdLeft = Config.blinkDagger.instance.getRemainingCooldown(stack);
						if (cdLeft == 0) {
							Config.blinkDagger.instance.startCooldown(stack, BlinkDagger.hurtCooldown, player);
						} else if (cdLeft < BlinkDagger.hurtCooldown) {
							Config.blinkDagger.instance.setRemainingCooldown(stack, BlinkDagger.hurtCooldown);
						}
					} // also Heart of Tarrasque
				}
			}
			
			// ... and some buffs are removed:
			onPlayerHurt(player);
		}
	}
	
	public static void onPlayerHurt(EntityPlayer player) {
		boolean shouldSync = false;
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		List<BuffInstance> buffs = stats.getAppliedBuffs(); // This list is a copy
		for (BuffInstance buffInst : buffs) {
			if (buffInst.buff == Buff.clarity || buffInst.buff == Buff.salve) {
				stats.removeBuff(buffInst);
				shouldSync = true;
			}
		}
		if (!player.worldObj.isRemote && shouldSync) {
			PacketDispatcher.sendPacketToPlayer(new PlayerHurtPacket().makePacket(), (Player)player);
		}
	}
}
