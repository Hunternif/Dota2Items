package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class OpenGuiPacket {
	//TODO: add proper x, y, z
	public static void sendOpenGuiPacket(int guiId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeShort(Dota2PacketID.GUI);
			outputStream.writeShort(guiId);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Dota2Items.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static boolean parseAndApplyOpenGuiPacket(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int guiId;
		
		try {
			if (inputStream.readShort() != Dota2PacketID.GUI) {
				return false;
			}
			guiId = inputStream.readShort();
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		player.openGui(Dota2Items.instance, guiId, player.worldObj, x, y, z);
		return true;
	}
}
