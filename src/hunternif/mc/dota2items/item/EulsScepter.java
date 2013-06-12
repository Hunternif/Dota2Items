package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.network.EntityMovePacket;
import hunternif.mc.dota2items.tileentity.TileEntityCyclone;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EulsScepter extends CooldownItem {
	public static final String NAME = "eulsScepter";
	
	public EulsScepter(int id) {
		super(id);
		setUnlocalizedName(NAME);
		setCooldown(30);
		passiveBuff = Buff.eulsScepter;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return onUseEulsScepter(stack, player, entity);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox((double)x, (double)y-0.5, (double)z, (double)x+1, (double)y+1.5, (double)z+1);
		List<EntityLiving> list = world.getEntitiesWithinAABB(EntityLiving.class, box);
		if (list != null && !list.isEmpty()) {
			//TODO Make sure that other entities within the area don't get stuck in the cyclone.
			return onUseEulsScepter(itemStack, player, list.get(0));
		} else {
			playDenyGeneralSound(world);
			return false;
		}
	}
	
	private boolean onUseEulsScepter(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!canUseItem(player)) {
			return false;
		}
		if (entity.isDead || !(entity instanceof EntityLiving)) {
			// Why would this ever happen?
			playDenyGeneralSound(player.worldObj);
			return false;
		}
		if (isOnCooldown(stack)) {
			playDenyCooldownSound(player.worldObj);
			return false;
		}
		EntityStats entityStats = Dota2Items.mechanics.getEntityStats((EntityLiving)entity);
		if (entityStats.isMagicImmune()) {
			playMagicImmuneSound(player.worldObj);
			return false;
		}
		
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		while (entity.worldObj.getBlockMaterial(x, y, z).isLiquid() && y < entity.worldObj.getHeight()) {
			y ++;
		}
		if (!entity.worldObj.isRemote) {
			entity.worldObj.setBlock(x, y, z, Dota2Items.cycloneContainer.blockID, 0, 3);
		}
		
		if (!entity.worldObj.isRemote) {
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;
			entity.posX = ((double) x) + 0.5;
			entity.posY = ((double) y) + 3;
			entity.posZ = ((double) z) + 0.5;
			EntityMovePacket.sendMovePacket(entity);
			
			long cycloneEndTime = entity.worldObj.getTotalWorldTime() + (long) (TileEntityCyclone.duration * 20f);
			BuffInstance buff = new BuffInstance(Buff.inCyclone, entity.entityId, cycloneEndTime, false);
			entityStats.addBuff(buff);
			PacketDispatcher.sendPacketToAllPlayers(buff.toPacket());
		}
		
		startCooldown(stack, player);
		player.worldObj.playSoundAtEntity(entity, Dota2ItemSounds.CYCLONE_START, 0.7f, 1);
		return true;
	}
}
