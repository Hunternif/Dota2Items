package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.ItemRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class ShopBuySetResultPacket extends CustomPacket{
	private int itemID;
	private int recipeItemID = 0;
	
	public ShopBuySetResultPacket() {}
	
	public ShopBuySetResultPacket(ItemStack stack) {
		itemID = stack.itemID;
		if (stack.itemID == Config.recipe.getID()) {
			recipeItemID = ItemRecipe.getItemID(stack);
		}
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(itemID);
		if (itemID == Config.recipe.getID()) {
			out.writeInt( recipeItemID );
		}
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		itemID = in.readInt();
		if (itemID == Config.recipe.getID()) {
			recipeItemID = in.readInt();
		}
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (!side.isClient()) {
			if (player.openContainer instanceof ContainerShopBuy) {
				ContainerShopBuy cont = (ContainerShopBuy)player.openContainer;
				if (itemID != Config.recipe.getID()) {
					cont.setResultItem((Dota2Item)Item.itemsList[itemID]);
				} else {
					cont.setResultItem(ItemRecipe.forItem(recipeItemID, false));
				}
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the client!");
		}
	}
}
