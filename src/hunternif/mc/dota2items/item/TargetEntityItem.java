package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.util.PositionUtil;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class TargetEntityItem extends TargetItem {
	private boolean canTargetEntitiesBelow = true;
	
	public TargetEntityItem(int id) {
		super(id);
	}
	
	public boolean getCanTargetEntitiesBelow() {
		return canTargetEntitiesBelow;
	}
	public ActiveItem setCanTargetEntitiesBelow(boolean value) {
		this.canTargetEntitiesBelow = value;
		return this;
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
	        
        	// Try hitting entity directly:
	        MovingObjectPosition hit = PositionUtil.tracePath(world,
					position.xCoord, position.yCoord, position.zCoord,
					lookFar.xCoord, lookFar.yCoord, lookFar.zCoord, 0, player, false);
			if (hit != null) {
				Entity entity = hit.entityHit;
				// If hit a block, try looking for entity above
				if (hit.typeOfHit == EnumMovingObjectType.TILE && getCanTargetEntitiesBelow()) {
					AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
							(double)hit.blockX, (double)hit.blockY-0.5, (double)hit.blockZ,
							(double)hit.blockX+1, (double)hit.blockY+1.5, (double)hit.blockZ+1);
					List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
					if (list != null && !list.isEmpty()) {
						entity = list.get(0);
					}
				}
				if (tryUse(stack, player, entity)) {
					MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
					if (!player.capabilities.isCreativeMode) {
						startCooldown(stack, player);
						Dota2Items.stats.getOrCreateEntityStats(player).removeMana(getManaCost());
					}
					onUseOnEntity(stack, player, (EntityLivingBase)entity);
				}
			}
		}
		return stack;
	}
	
	/**
	 * Returns the Sound that signifies the particular reason why the player
	 * cannot use this item.
	 */
	protected Sound canUseItem(ItemStack stack, EntityLivingBase player, Entity target) {
		Sound failSound = canUseItem(stack, player);
		if (failSound == null) {
			if (target != null) {
				// Only work on living entities...
				if (!(target instanceof EntityLivingBase) ||
						// ... if their health is > 0 
						((EntityLivingBase)target).getHealth() <= 0) {
					return Sound.DENY_GENERAL;
				}
				EntityStats targetStats = Dota2Items.stats.getOrCreateEntityStats((EntityLivingBase)target);
				if (targetStats.isMagicImmune()) {
					return Sound.MAGIC_IMMUNE;
				}
			} else {
				return Sound.DENY_GENERAL;
			}
			if (player instanceof EntityPlayer && ((EntityPlayer)player).capabilities.isCreativeMode) {
				return null;
			}
		}
		return failSound;
	}
	
	/**
	 * If the player cannot use this item, a respective sound is played.
	 * @return whether the player can use this item.
	 */
	public boolean tryUse(ItemStack stack, EntityLivingBase player, Entity target) {
		Sound failSound = canUseItem(stack, player, target);
		if (failSound != null && player.worldObj.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(failSound.getName(), 1.0F, 1.0F);
		}
		return failSound == null;
	}
	
	protected abstract void onUseOnEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity);
}
