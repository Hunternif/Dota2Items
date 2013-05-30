package hunternif.mc.dota2items.mechanics.buff;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.mechanics.Dota2EntityStats;
import hunternif.mc.dota2items.network.Dota2PacketID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class Dota2EntityBuff {
	public static final int NONE = -1;
	
	public static final String CYCLONE = "cyclone";
	
	private static List<String> buffList = new ArrayList<String>();
	static {
		buffList.add(CYCLONE);
	}
	
	public static int getBuffId(String name) {
		int id = buffList.indexOf(name);
		if (id != -1) {
			return id;
		} else {
			return NONE;
		}
	}
	
	public static String getBuffName(int id) {
		try {
			return buffList.get(id);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	
	public final String name;
	public final Dota2EntityStats stats;
	public final long endTime;
	public Dota2EntityBuff(String name, Dota2EntityStats stats, long timeout) {
		this.name = name;
		this.stats = stats;
		this.endTime = stats.entity.worldObj.getTotalWorldTime() + timeout;
	}
	
	public abstract void setStartStats();
	
	public abstract void setEndStats();
	
	public Packet250CustomPayload toPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.BUFF);
			outputStream.writeInt(getBuffId(name));
			outputStream.writeInt(MathHelper.floor_double(stats.entity.entityId));
			outputStream.writeLong(endTime - stats.entity.worldObj.getTotalWorldTime());
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
	
	public static Dota2EntityBuff fromPacket(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		String name;
		int entityID;
		long timeout;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.BUFF)
				return null;
			name = Dota2EntityBuff.getBuffName(inputStream.readInt());
			entityID = inputStream.readInt();
			timeout = inputStream.readLong();
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Dota2EntityStats stats = Dota2Items.mechanics.getEntityStatsClient(entityID);
		if (stats == null) {
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
				stats = new Dota2EntityStats(entity);
			} else {
				return null;
			}
		}
		if (name.equals(CYCLONE)) {
			return new BuffCyclone(stats, timeout);
		} else {
			return null;
		}
	}
}
