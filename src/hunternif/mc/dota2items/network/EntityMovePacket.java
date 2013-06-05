package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class EntityMovePacket {
	public static void sendMovePacket(Entity entity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
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
	
	//NOTE I hope I can get rid of this
	public static boolean parseAndApplyEntityMove(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int entityID;
		double x;
		double y;
		double z;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.MOVE)
				return false;
			entityID = inputStream.readInt();
			x = ((double) inputStream.readInt()) + 0.5;
			y = ((double) inputStream.readInt());
			z = ((double) inputStream.readInt()) + 0.5;
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null) {
				//TODO fix the squids teleporting back underwater when the cyclone ends.
				entity.setPosition(x, y, z);
				return true;
			}
		}
		return false;
	}
}
