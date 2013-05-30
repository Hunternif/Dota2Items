package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.mechanics.Dota2EntityStats;
import hunternif.mc.dota2items.mechanics.buff.BuffCyclone;
import hunternif.mc.dota2items.network.Dota2PacketID;
import hunternif.mc.dota2items.tileentity.TileEntityCyclone;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
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
		//moveSpeedFactor = 1.5f;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!canUseItem(player)) {
			return false;
		}
		if (entity.isDead) {
			return false;
		}
		if (isOnCooldown(stack)) {
			playDenyCooldownSound(player.worldObj);
			return false;
		}
		Dota2EntityStats stats = Dota2Items.mechanics.getEntityStats(entity);
		if (stats == null) {
			stats = new Dota2EntityStats(entity);
			Dota2Items.mechanics.putEntityStats(stats);
		}
		if (stats.isMagicImmune()) {
			return false;
		}
		
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		while (entity.worldObj.getBlockMaterial(x, y, z).isLiquid() && y < entity.worldObj.getHeight()) {
			y ++;
		}
		if (!entity.worldObj.isRemote) {
			entity.worldObj.setBlock(x, y, z, Dota2Items.cycloneContainer.blockID);
		}
		
		if (!entity.worldObj.isRemote) {
			/*int x32 = entity.myEntitySize.multiplyBy32AndRound(entity.posX);
			int y32 = MathHelper.floor_double(entity.posY * 32);
			int z32 = entity.myEntitySize.multiplyBy32AndRound(entity.posZ);
			byte yawByte = (byte) MathHelper.floor_float(entity.rotationYaw * 256.0F / 360.0F);
            byte pitchByte = (byte) MathHelper.floor_float(entity.rotationPitch * 256.0F / 360.0F);
			Packet34EntityTeleport tpPacket = new Packet34EntityTeleport(entity.entityId, x32, y32, z32, yawByte, pitchByte);
			PacketDispatcher.sendPacketToAllPlayers(tpPacket);*/
			
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;
			entity.posX = ((double) x) + 0.5;
			entity.posY = ((double) y) + 3;
			entity.posZ = ((double) z) + 0.5;
			sendMovePacket(entity);
			
			int timeout = (int) (TileEntityCyclone.duration * 20f);
			BuffCyclone buff = new BuffCyclone(stats, timeout);
			Dota2Items.mechanics.addBuff(buff);
			PacketDispatcher.sendPacketToAllPlayers(buff.toPacket());
		}
		
		startCooldown(stack, player);
		player.worldObj.playSoundAtEntity(entity, Dota2ItemSounds.CYCLONE_START, 0.7f, 1);
		return true;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox((double)x, (double)y-0.5, (double)z, (double)x+1, (double)y+1.5, (double)z+1);
		List<EntityLiving> list = world.getEntitiesWithinAABB(EntityLiving.class, box);
		if (list != null && !list.isEmpty()) {
			return onLeftClickEntity(itemStack, player, list.get(0));
		} else {
			playDenyGeneralSound(world);
			return false;
		}
	}
	
	private void sendMovePacket(Entity entity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.MOVE);
			outputStream.writeInt(entity.entityId);
			outputStream.writeInt(MathHelper.floor_double(entity.posX));
			outputStream.writeInt(MathHelper.floor_double(entity.posY));
			outputStream.writeInt(MathHelper.floor_double(entity.posZ));
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Dota2Items.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}
}
