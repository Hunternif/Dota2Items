package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.event.UseItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class TargetBlockItem extends TargetItem {
	public TargetBlockItem(int id) {
		super(id);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (getCastRange() > 0) {
			Vec3 position = world.getWorldVec3Pool().getVecFromPool(player.posX, player.posY, player.posZ);
			if (!world.isRemote) {
				// Because in server worlds the Y coordinate of a player is his feet's coordinate, without yOffset.
				position = position.addVector(0, 1.62D, 0);
			}
	        Vec3 look = player.getLook(1.0F);
	        Vec3 lookFar = position.addVector(look.xCoord * getCastRange(), look.yCoord * getCastRange(), look.zCoord * getCastRange());
	        
        	// This will change position!
	        MovingObjectPosition hit = world.clip(position, lookFar);
			if (hit != null && tryUse(stack, player, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit)) {
				MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
				if (!player.capabilities.isCreativeMode) {
					startCooldown(stack, player);
					Dota2Items.stats.getOrCreateEntityStats(player).removeMana(getManaCost());
				}
				onUseOnBlock(stack, player, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit);
			}
		}
		return stack;
	}
	
	/**
	 * Returns the Sound that signifies the particular reason why the player
	 * cannot use this item.
	 */
	protected Sound canUseItem(ItemStack stack, EntityLivingBase player, int x, int y, int z, int side) {
		return canUseItem(stack, player);
	}
	
	/**
	 * If the player cannot use this item, a respective sound is played.
	 * @return whether the player can use this item.
	 */
	public boolean tryUse(ItemStack stack, EntityLivingBase player, int x, int y, int z, int side) {
		Sound failSound = canUseItem(stack, player, x, y, z, side);
		if (failSound != null && player.worldObj.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(failSound.getName(), 1.0F, 1.0F);
		}
		return failSound == null;
	}
	
	protected abstract void onUseOnBlock(ItemStack stack, EntityPlayer player, int x, int y, int z, int side);
}
