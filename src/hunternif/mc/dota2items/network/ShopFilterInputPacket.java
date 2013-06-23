package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.gui.GuiShopBuy;
import hunternif.mc.dota2items.inventory.ContainerShopBuy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ShopFilterInputPacket {
	public static void sendToServer(String filterStr) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeShort(Dota2PacketID.SHOP_FILTER_INPUT);
			Packet.writeString(filterStr, outputStream);
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
	
	public static boolean parseAndApply(Packet250CustomPayload packet, EntityPlayer player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		String filterStr;
		
		try {
			if (inputStream.readShort() != Dota2PacketID.SHOP_FILTER_INPUT) {
				return false;
			}
			filterStr = Packet.readString(inputStream, GuiShopBuy.FILTER_STR_LENGTH);
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (player.openContainer instanceof ContainerShopBuy) {
			ContainerShopBuy cont = (ContainerShopBuy)player.openContainer;
			cont.invShop.setFilterStr(filterStr);
		}
		return true;
	}
}
