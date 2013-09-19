package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.effect.Effect;
import hunternif.mc.dota2items.effect.EffectInstance;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.network.BuffForcePacket;
import hunternif.mc.dota2items.util.BlockUtil;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ForceStaff extends CooldownItem {
	public static final String TAG_YAW = "yaw";
	
	private static final byte FLAG_MOVED_UP = 1;
	private static final byte FLAG_MOVED_DOWN = 2;
	
	public static final float forceDuration = 0.25f;
	public static final double forceSpeed = 3;
	
	public ForceStaff(int id) {
		super(id);
		setCooldown(10);
		setManaCost(25);
		MinecraftForge.EVENT_BUS.register(this);
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
		Dota2Items.stats.getOrCreateEntityStats(player).removeMana(getManaCost());
		if (!entity.worldObj.isRemote) {
			boolean usingOnSelf = player == entity;
			entity.worldObj.playSoundAtEntity(entity, Sound.FORCE_STAFF.getName(), 0.5f, 1);
			long startTime = entity.worldObj.getTotalWorldTime();
			long endTime = startTime + (long) (forceDuration * MCConstants.TICKS_PER_SECOND);
			BuffInstance buffInst = new BuffInstance(Buff.force, entity.entityId, startTime, endTime, usingOnSelf);
			buffInst.tag.setFloat(TAG_YAW, entity.rotationYaw);
			EntityStats entityStats = Dota2Items.stats.getOrCreateEntityStats((EntityLivingBase)entity);
			entityStats.addBuff(buffInst);
			PacketDispatcher.sendPacketToAllPlayers(new BuffForcePacket(buffInst).makePacket());
		}
		return true;
	}
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		EntityStats stats = Dota2Items.stats.getEntityStats(event.entityLiving);
		if (stats != null) {
			BuffInstance buffForce = stats.getBuffInstance(Buff.force);
			if (buffForce != null) {
				processForceMovement(event.entity, buffForce.tag.getFloat(ForceStaff.TAG_YAW));
			}
		}
	}
	
	public static void processForceMovement(Entity entity, float yaw) {
		if (entity.ridingEntity != null) {
			entity = entity.ridingEntity;
		}
		float sinYaw = MathHelper.sin(yaw * (float)Math.PI / 180.0F);
		float cosYaw = MathHelper.cos(yaw * (float)Math.PI / 180.0F);
		
		AxisAlignedBB startBb = entity.boundingBox.copy();
		AxisAlignedBB bb = null;
		double distance = -1;
		while (distance < forceSpeed) {
			byte flags = 0;
			distance ++;
			if (distance > forceSpeed) {
				distance = forceSpeed;
			}
			bb = startBb.getOffsetBoundingBox(-distance * sinYaw, 0, distance * cosYaw);
			// Make sure that we're not moving inside blocks:
			while (!entity.worldObj.getCollidingBlockBounds(bb).isEmpty() && bb.minY > 0) {
				bb.offset(0, 1, 0);
				flags |= FLAG_MOVED_UP;
			}
			// Make sure that we're not flying in the air:
			while (!BlockUtil.isSolid(entity.worldObj, (bb.minX + bb.maxX)/2d, bb.minY - 1, (bb.minZ + bb.maxZ)/2d)
					&& bb.maxY < entity.worldObj.getActualHeight()) {
				bb.offset(0, -1, 0);
				flags |= FLAG_MOVED_DOWN;
			}
			if (bb.minY <= 0 || bb.maxY >= entity.worldObj.getActualHeight()) {
				return;
			}
			double oldPosX = entity.posX;
			double oldPosY = entity.posY - entity.yOffset;
			double oldPosZ = entity.posZ;
			double dx = (bb.minX + bb.maxX)/2d - oldPosX;
			double dy = bb.minY - oldPosY;
			double dz = (bb.minZ + bb.maxZ)/2d - oldPosZ;
			if ((flags & FLAG_MOVED_UP) != 0) {
				entity.moveEntity(0, dy, 0);
				if (entity.worldObj.isRemote) {
					new EffectInstance(Effect.force,
							oldPosX, oldPosY, oldPosZ,
							0, entity.posY - entity.yOffset - oldPosY, 0)
					.perform();
				}
				oldPosY = entity.posY - entity.yOffset;
			}
			entity.moveEntity(dx, 0, dz);
			if (entity.worldObj.isRemote) {
				new EffectInstance(Effect.force,
						oldPosX, oldPosY, oldPosZ,
						entity.posX - oldPosX, 0, entity.posZ - oldPosZ)
				.perform();
			}
			oldPosX = entity.posX;
			oldPosZ = entity.posZ;
			if ((flags & FLAG_MOVED_DOWN) != 0) {
				entity.moveEntity(0, dy, 0);
				if (entity.worldObj.isRemote) {
					new EffectInstance(Effect.force,
							oldPosX, oldPosY, oldPosZ,
							0, entity.posY - entity.yOffset - oldPosY, 0)
					.perform();
				}
			}
			entity.fallDistance = 0;
		}
		//System.out.println((entity.worldObj.isRemote ? "client" : "server") + ": forced entity to " + entity.toString());
	}
}
