package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.network.BuffPacket;
import hunternif.mc.dota2items.network.EntityMovePacket;
import hunternif.mc.dota2items.tileentity.TileEntityCyclone;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.network.PacketDispatcher;

public class EulsScepter extends TargetEntityItem {
	public EulsScepter(int id) {
		super(id);
		setCooldown(30);
		setManaCost(75);
		setCastRange(12);
	}
	
	@Override
	protected void onUseOnEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		while (entity.worldObj.getBlockMaterial(x, y, z).isLiquid() && y < entity.worldObj.getHeight()) {
			y ++;
		}
		if (!entity.worldObj.isRemote) {
			entity.worldObj.setBlock(x, y, z, Config.cycloneContainer.getID(), 0, 3);
		}
		
		if (!entity.worldObj.isRemote) {
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;
			entity.setPosition(((double) x) + 0.5, ((double) y) + 3, ((double) z) + 0.5);
			entity.lastTickPosX = entity.prevPosX = entity.posX;
			entity.lastTickPosY = entity.prevPosY = entity.posY;
			entity.lastTickPosZ = entity.prevPosZ = entity.posZ;
			PacketDispatcher.sendPacketToAllPlayers(new EntityMovePacket(entity).makePacket());
			
			long startTime = entity.worldObj.getTotalWorldTime();
			long cycloneEndTime = startTime + (long) (TileEntityCyclone.duration * MCConstants.TICKS_PER_SECOND);
			boolean usingOnSelf = player == entity;
			BuffInstance buffInst = new BuffInstance(Buff.inCyclone, entity.entityId, startTime, cycloneEndTime, usingOnSelf);
			EntityStats entityStats = Dota2Items.stats.getOrCreateEntityStats((EntityLivingBase)entity);
			entityStats.addBuff(buffInst);
			PacketDispatcher.sendPacketToAllPlayers(new BuffPacket(buffInst).makePacket());
		}
		player.worldObj.playSoundAtEntity(entity, Sound.CYCLONE_START.getName(), 0.7f, 1);
	}
}
