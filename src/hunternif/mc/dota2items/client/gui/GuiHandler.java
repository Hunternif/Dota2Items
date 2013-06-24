package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.inventory.ContainerShopBuy;
import hunternif.mc.dota2items.inventory.ContainerShopSell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int GUI_ID_SHOP_BUY = 0;
	public static final int GUI_ID_SHOP_SELL = 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_ID_SHOP_BUY:
			return new ContainerShopBuy();
		case GUI_ID_SHOP_SELL:
			return new ContainerShopSell(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_ID_SHOP_BUY:
			return new GuiShopBuy(player.inventory);
		case GUI_ID_SHOP_SELL:
			return new GuiShopSell(player.inventory);
		}
		return null;
	}

}
