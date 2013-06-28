package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class ShopBuySetFilterPacket extends CustomPacket {
	
	private String filterStr;
	
	public ShopBuySetFilterPacket() {}
	
	public ShopBuySetFilterPacket(String filterStr) {
		this.filterStr = filterStr;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeUTF(filterStr);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		filterStr = in.readUTF();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (!side.isClient()) {
			if (player.openContainer instanceof ContainerShopBuy) {
				ContainerShopBuy cont = (ContainerShopBuy)player.openContainer;
				cont.invShop.setFilterStr(filterStr);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the client!");
		}
	}
}
