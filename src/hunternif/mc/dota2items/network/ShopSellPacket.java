package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopSell;
import hunternif.mc.dota2items.item.Dota2Item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ShopSellPacket {
	public static void sendToServer() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeShort(Dota2PacketID.SHOP_SELL);
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
		
		try {
			if (inputStream.readShort() != Dota2PacketID.SHOP_SELL) {
				return false;
			}
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (player.openContainer instanceof ContainerShopSell) {
			ContainerShopSell cont = (ContainerShopSell)player.openContainer;
			ItemStack stackOnSale = cont.getSellingSlot().getStack();
			int sellPrice = Dota2Item.getSellPrice(stackOnSale);
			cont.putStackInSlot(0, null);
			EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
			stats.addGold(sellPrice);
		}
		return true;
	}
}
