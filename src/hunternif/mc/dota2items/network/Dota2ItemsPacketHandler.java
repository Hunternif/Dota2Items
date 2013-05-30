package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.effect.Dota2Effect;
import hunternif.mc.dota2items.mechanics.buff.Dota2EntityBuff;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class Dota2ItemsPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(Dota2Items.CHANNEL)) {
			
			// Try parsing effect
			Dota2Effect effect = Dota2Effect.fromPacket(packet);
			if (effect != null) {
				effect.render();
				return;
			}
			
			// Try parsing buff
			Dota2EntityBuff buff = Dota2EntityBuff.fromPacket(packet);
			if (buff != null) {
				Dota2Items.mechanics.addBuff(buff);
			}
			
			//try parsing entity move
			parseEntityMove(packet);
		}
	}
	
	private static boolean parseEntityMove(Packet250CustomPayload packet) {
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
				entity.posX = x;
				entity.posY = y;
				entity.posZ = z;
				return true;
			}
		}
		return false;
	}
}
