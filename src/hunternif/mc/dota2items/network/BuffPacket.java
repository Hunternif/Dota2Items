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
	private int buffID;
	private int entityID;
	private long endTime;
	private boolean isItemPassiveBuff;
	
	public BuffPacket() {}
	
	public BuffPacket(BuffInstance inst) {
		buffID = inst.buff.id;
		entityID = inst.entityID;
		endTime = inst.endTime;
		isItemPassiveBuff = inst.isItemPassiveBuff;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(buffID);
		out.writeInt(entityID);
		out.writeLong(endTime);
		out.writeBoolean(isItemPassiveBuff);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		buffID = in.readInt();
		entityID = in.readInt();
		endTime = in.readLong();
		isItemPassiveBuff = in.readBoolean();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				EntityStats stats = Dota2Items.mechanics.getEntityStats((EntityLivingBase)entity);
				BuffInstance buffInst = new BuffInstance(Buff.buffList[buffID], entityID, endTime, isItemPassiveBuff);
				stats.addBuff(buffInst);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}

}
