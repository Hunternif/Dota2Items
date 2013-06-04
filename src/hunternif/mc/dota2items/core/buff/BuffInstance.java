package hunternif.mc.dota2items.core.buff;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.network.Dota2PacketID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;


public class BuffInstance {
	public int entityID;
	public Buff buff;
	public long endTime;
	public boolean isItemPassiveBuff;
	
	public BuffInstance(Buff buff, int entityID, long endTime) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = endTime;
	}
	
	public BuffInstance(Buff buff, int entityID) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = -1;
	}
	
	public BuffInstance(Buff buff, int entityID, long endTime, boolean isItemPassiveBuff) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = endTime;
		this.isItemPassiveBuff = isItemPassiveBuff;
	}
	
	public BuffInstance(Buff buff, int entityID, boolean isItemPassiveBuff) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = -1;
		this.isItemPassiveBuff = isItemPassiveBuff;
	}
	
	public boolean isPermanent() {
		return endTime < 0;
	}
	
	public Packet250CustomPayload toPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.BUFF);
			outputStream.writeInt(buff.id);
			outputStream.writeInt(entityID);
			outputStream.writeLong(endTime);
			outputStream.writeBoolean(isItemPassiveBuff);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Dota2Items.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;
	}
	
	public static BuffInstance fromPacket(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int buffID;
		int entityID;
		long endTime;
		boolean isItemPassiveBuff;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.BUFF)
				return null;
			buffID = inputStream.readInt();
			entityID = inputStream.readInt();
			endTime = inputStream.readLong();
			isItemPassiveBuff = inputStream.readBoolean();
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new BuffInstance(Buff.buffList[buffID], entityID, endTime, isItemPassiveBuff);
	}
}
