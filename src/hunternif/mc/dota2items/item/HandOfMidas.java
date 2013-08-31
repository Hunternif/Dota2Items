package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class HandOfMidas extends CooldownItem {

	public HandOfMidas(int id) {
		super(id);
		setCooldown(100);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!tryUse(stack, player, entity)) {
			return false;
		}
		entity.setDead();
		player.playSound(Sound.HAND_OF_MIDAS.getName(), 1, 1);
		startCooldown(stack, player);
		
		// Drop 2.5 * regular experience
		if (!entity.worldObj.isRemote) {
			int exp = MathHelper.floor_double(2.5 * (1 + entity.worldObj.rand.nextInt(3)));
			while (exp > 0) {
				int expPart = EntityXPOrb.getXPSplit(exp);
				exp -= expPart;
				entity.worldObj.spawnEntityInWorld(new EntityXPOrb(entity.worldObj, entity.posX, entity.posY, entity.posZ, expPart));
			}
		}
		// Add 190 reliable gold
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
		stats.addGold(190, 0);
		return true;
	}
	
}
