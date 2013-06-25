package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.ItemRecipe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ShopSetResultPacket {
	public static void sendToServer(ItemStack stack) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeShort(Dota2PacketID.SHOP_SET_RESULT);
			outputStream.writeInt(stack.itemID);
			if (stack.itemID == Config.recipe.getID()) {
				outputStream.writeInt( ItemRecipe.getItemID(stack) );
			}
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
		int itemID;
		boolean isRecipe;
		int recipeItemID = 0;
		
		try {
			if (inputStream.readShort() != Dota2PacketID.SHOP_SET_RESULT) {
				return false;
			}
			itemID = inputStream.readInt();
			isRecipe = itemID == Config.recipe.getID();
			if (isRecipe) {
				recipeItemID = inputStream.readInt();
			}
			inputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (player.openContainer instanceof ContainerShopBuy) {
			ContainerShopBuy cont = (ContainerShopBuy)player.openContainer;
			if (!isRecipe) {
				cont.setResultItem((Dota2Item)Item.itemsList[itemID]);
			} else {
				cont.setResultItem(ItemRecipe.forItem(recipeItemID, false));
			}
		}
		return true;
	}
}
