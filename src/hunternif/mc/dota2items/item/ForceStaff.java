package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.client.particle.ParticleDust;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.network.BuffForcePacket;
import hunternif.mc.dota2items.util.BlockUtil;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ForceStaff extends CooldownItem {
	public static final String TAG_YAW = "yaw";
	
	public static final float forceDuration = 0.25f;
	public static final double forceSpeed = 2.5;
	public static final int puffsPerBlock = 2;
	private static final double trailStep = 1 / ((double) puffsPerBlock);
	
	public ForceStaff(int id) {
		super(id);
		setCooldown(10);
		setManaCost(25);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return onUseForceStaff(stack, player, entity);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		onUseForceStaff(stack, player, player);
		return stack;
	}
	
	private boolean onUseForceStaff(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!tryUse(stack, player, entity)) {
			return false;
		}
		MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
		startCooldown(stack, player);
		Dota2Items.mechanics.getOrCreateEntityStats(player).removeMana(getManaCost());
		if (!entity.worldObj.isRemote) {
			boolean usingOnSelf = player == entity;
			entity.worldObj.playSoundAtEntity(entity, Sound.FORCE_STAFF.getName(), 0.5f, 1);
			long startTime = entity.worldObj.getTotalWorldTime();
			long endTime = startTime + (long) (forceDuration * MCConstants.TICKS_PER_SECOND);
			BuffInstance buffInst = new BuffInstance(Buff.force, entity.entityId, startTime, endTime, usingOnSelf);
			buffInst.tag.setFloat(TAG_YAW, player.rotationYaw);
			EntityStats entityStats = Dota2Items.mechanics.getOrCreateEntityStats((EntityLivingBase)entity);
			entityStats.addBuff(buffInst);
			PacketDispatcher.sendPacketToAllPlayers(new BuffForcePacket(buffInst).makePacket());
		}
		return true;
	}
	
	public static void processForceMovement(Entity entity, float yaw) {
		float sinYaw = MathHelper.sin(yaw * (float)Math.PI / 180.0F);
		float cosYaw = MathHelper.cos(yaw * (float)Math.PI / 180.0F);
		
		Vec3 startPos = entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY - entity.yOffset, entity.posZ);
		Vec3 endPos = startPos;
		double distance = -1;
		while (distance < forceSpeed) {
			distance ++;
			if (distance > forceSpeed) {
				distance = forceSpeed;
			}
			endPos = startPos.addVector(-distance * sinYaw, 0, distance * cosYaw);
			IntVec3 coords = new IntVec3(endPos);
			// Make sure that we're not moving inside a block:
			if (BlockUtil.isSolid(entity.worldObj, coords.x, coords.y, coords.z)) {
				coords = BlockUtil.findSurfaceUpward(entity.worldObj, coords.x, coords.y, coords.z);
				if (coords != null) {
					startPos.yCoord = coords.y;
					endPos.yCoord = coords.y;
				} else {
					return;
				}
			}
			// Make sure that we're not flying in the air:
			if (!BlockUtil.isSolid(entity.worldObj, coords.x, coords.y-1, coords.z)) {
				coords = BlockUtil.findSurfaceDownward(entity.worldObj, coords.x, coords.y-1, coords.z);
				if (coords != null) {
					startPos.yCoord = coords.y;
					endPos.yCoord = coords.y;
				} else {
					return;
				}
			}
			double oldPosX = entity.posX;
			double oldPosY = entity.posY - entity.yOffset;
			double oldPosZ = entity.posZ;
			double dx = endPos.xCoord - entity.posX;
			double dy = endPos.yCoord - entity.posY + entity.yOffset;
			double dz = endPos.zCoord - entity.posZ;
			entity.moveEntity(0, dy, 0);
			trailEffect(entity.worldObj, oldPosX, oldPosY, oldPosZ, 0, entity.posY - entity.yOffset - oldPosY, 0);
			entity.moveEntity(dx, 0, dz);
			trailEffect(entity.worldObj, oldPosX, oldPosY, oldPosZ, entity.posX - oldPosX, 0, entity.posZ - oldPosZ);
		}
	}
	
	public static void trailEffect(World world, double oldPosX, double oldPosY, double oldPosZ,
			double dx, double dy, double dz) {
		if (world.isRemote) {
			EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
			double d = Math.sqrt(dx*dx + dy*dy + dz*dz);
			for (double i = 0; i < d; i += trailStep) {
				EntityFX particle = new ParticleDust(world,
						oldPosX + dx * i/d,
						oldPosY + dy * i/d,
						oldPosZ + dz * i/d, 0, 0, 0);
				effectRenderer.addEffect(particle);
			}
		}
	}
}
