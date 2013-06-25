package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class EntityStatsPacket {
	public static Map<EntityStats, Integer> sentAtTicks = new ConcurrentHashMap<EntityStats, Integer>();
	public static boolean lastSentAt(EntityStats stats, int tick) {
		Integer lastTickSent = sentAtTicks.get(stats);
		return lastTickSent != null && lastTickSent.intValue() == tick;
	}
	
	public static void sendEntityStatsPacket(EntityStats stats) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.STATS);
			outputStream.writeInt(stats.entityId);
			outputStream.writeFloat(stats.partialHalfHeart);
			outputStream.writeInt(MathHelper.floor_float(stats.getFloatMana()*100f));
			outputStream.writeInt(MathHelper.floor_float(stats.getFloatGold()*100f));
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
	
	public static boolean parseAndApplyEntityStats(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int entityID;
		float partialHalfHeart;
		float mana;
		float gold;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.STATS) {
				return false;
			}
			entityID = inputStream.readInt();
			partialHalfHeart = inputStream.readFloat();
			mana = (float)inputStream.readInt() / 100f;
			gold = (float)inputStream.readInt() / 100f;
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLiving) {
				EntityStats stats = Dota2Items.mechanics.getEntityStats((EntityLiving)entity);
				stats.partialHalfHeart = partialHalfHeart;
				stats.setGold(gold);
				stats.setMana(mana);
				return true;
			}
		}
		return false;
	}
}
