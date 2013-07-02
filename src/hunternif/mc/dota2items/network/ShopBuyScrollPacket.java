package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class ShopBuyScrollPacket extends CustomPacket {
	
	private int scrollRow;
	
	public ShopBuyScrollPacket() {}
	
	public ShopBuyScrollPacket(int scrollRow) {
		this.scrollRow = scrollRow;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(scrollRow);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		scrollRow = in.readInt();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (!side.isClient()) {
			if (player.openContainer instanceof ContainerShopBuy) {
				ContainerShopBuy cont = (ContainerShopBuy)player.openContainer;
				cont.invShop.scrollToRow(scrollRow);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the client!");
		}
	}
}
