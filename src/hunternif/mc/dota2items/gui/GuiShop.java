package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.inventory.ContainerShop;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiShop extends GuiContainer {

	public GuiShop(InventoryPlayer inventoryPlayer, EntityShopkeeper shopkeeper) {
		super(new ContainerShop(inventoryPlayer, shopkeeper));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Auto-generated method stub
		System.out.println("drawing the Dota 2 shop");
	}

}
