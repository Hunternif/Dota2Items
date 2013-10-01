package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class BuffPacket extends CustomPacket {
	protected int buffID;
	protected int entityID;
	protected long startTime;
	protected long endTime;
	protected boolean isFriendly;
	
	public BuffPacket() {}
	
	public BuffPacket(BuffInstance inst) {
		buffID = inst.buff.id;
		entityID = inst.entity.entityId;
		startTime = inst.startTime;
		endTime = inst.endTime;
		isFriendly = inst.isFriendly;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(buffID);
		out.writeInt(entityID);
		out.writeLong(startTime);
		out.writeLong(endTime);
		out.writeBoolean(isFriendly);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		buffID = in.readInt();
		entityID = in.readInt();
		startTime = in.readLong();
		endTime = in.readLong();
		isFriendly = in.readBoolean();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				EntityStats stats = Dota2Items.stats.getOrCreateEntityStats((EntityLivingBase)entity);
				BuffInstance buffInst = new BuffInstance(Buff.buffList[buffID], (EntityLivingBase)entity, startTime, endTime, isFriendly);
				stats.addBuff(buffInst);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
