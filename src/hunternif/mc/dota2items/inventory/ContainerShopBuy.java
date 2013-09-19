package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.ItemRecipe;
import hunternif.mc.dota2items.network.ShopBuySetResultPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ContainerShopBuy extends Container {
	private static final int SHOP_INV_X = 8;
	private static final int SHOP_INV_Y = 60;
	private static final int PLAYER_INV_X = 35;
	private static final int PLAYER_INV_Y = 215;
	
	public InventoryShop invShop = InventoryShop.newFullShop(4);
	private InventoryBasic invResult = new InventoryBasic("Buying", false, 1);
	private InventoryPlayer invPlayer;
	protected int slotResultNumber;
	
	public ContainerShopBuy(InventoryPlayer inventoryPlayer) {
		this.invPlayer = inventoryPlayer;
		// Assign shopkeeper's inventory
		for (int i = 0; i < invShop.getRows(); i++) {
			for (int j = 0; j < invShop.getColumns(); j++) {
				addSlotToContainer(new SlotShop(invShop, i * invShop.getColumns() + j, SHOP_INV_X + j * 18, SHOP_INV_Y + i * 18));
			}
		}
		// Assign player's hotbar
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventoryPlayer, i, PLAYER_INV_X + i * 18, PLAYER_INV_Y));
		}
		slotResultNumber = addSlotToContainer(new SlotShopBuyResult(invResult, 0, 167, 175)).slotNumber;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public SlotShopBuyResult getSlotResult() {
		return (SlotShopBuyResult)inventorySlots.get(slotResultNumber);
	}
	
	public ItemStack getResultItem() {
		return invResult.getStackInSlot(0);
	}
	public void setResultItem(Dota2Item item) {
		ItemStack stack = null;
		if (item != null) {
			if (invShop.contains(item)) {
				stack = new ItemStack(item, ((Dota2Item)item).getDefaultQuantity());
				invResult.setInventorySlotContents(0, stack);
			}
		} else {
			invResult.setInventorySlotContents(0, null);
		}
		if (invPlayer.player.worldObj.isRemote) {
			PacketDispatcher.sendPacketToServer(new ShopBuySetResultPacket(stack).makePacket());
		}
	}
	public void setResultItem(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof Dota2Item) {
				setResultItem((Dota2Item)stack.getItem());
			} else if (stack.itemID == Config.recipe.getID()) {
				invResult.setInventorySlotContents(0, ItemRecipe.copy(stack, false));
				if (invPlayer.player.worldObj.isRemote) {
					PacketDispatcher.sendPacketToServer(new ShopBuySetResultPacket(stack).makePacket());
				}
			}
		} else {
			invResult.setInventorySlotContents(0, null);
			if (invPlayer.player.worldObj.isRemote) {
				PacketDispatcher.sendPacketToServer(new ShopBuySetResultPacket(null).makePacket());
			}
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}
}
