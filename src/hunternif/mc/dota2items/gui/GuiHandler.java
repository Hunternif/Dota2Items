package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.inventory.ContainerShop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int GUI_ID_SHOP = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_SHOP) {
			return new ContainerShop(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_SHOP) {
			return new GuiShop(player.inventory);
		}
		return null;
	}

}
