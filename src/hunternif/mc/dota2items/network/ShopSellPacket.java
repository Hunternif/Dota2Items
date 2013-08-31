package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.inventory.ContainerShopSell;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class ShopSellPacket extends CustomPacket {
	
	public ShopSellPacket() {}
	
	@Override
	public void write(ByteArrayDataOutput out) {}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (!side.isClient()) {
			if (player.openContainer instanceof ContainerShopSell) {
				ContainerShopSell cont = (ContainerShopSell)player.openContainer;
				ItemStack stackOnSale = cont.getSellingSlot().getStack();
				int sellPrice = Dota2Item.getSellPrice(stackOnSale);
				cont.putStackInSlot(0, null);
				EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(player);
				// Reliable gold:
				stats.addGold(sellPrice, 0);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the client!");
		}
	}
}
