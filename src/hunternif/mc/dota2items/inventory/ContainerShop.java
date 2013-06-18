package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.entity.EntityShopkeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerShop extends Container {
	public static final float MAX_DISTANCE = 32;
	
	private InventoryPlayer inventoryPlayer;
	private EntityShopkeeper shopkeeper;
	
	public ContainerShop(InventoryPlayer inventoryPlayer, EntityShopkeeper shopkeeper) {
		this.inventoryPlayer = inventoryPlayer;
		this.shopkeeper = shopkeeper;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return entityplayer.getDistanceToEntity(shopkeeper) < MAX_DISTANCE;
	}

}
