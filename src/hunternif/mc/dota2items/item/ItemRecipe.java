package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.inventory.InventoryShop;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRecipe extends Item {
	private static final String TAG_ITEM_ID = "D2IRecipeID";
	
	public ItemRecipe(int id) {
		super(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Dota2Items.ID + ":recipe");
	}
	
	public static ItemStack forItem(Dota2Item item, boolean isSample) {
		return forItem(item.itemID, isSample);
	}
	public static ItemStack forItem(int itemID, boolean isSample) {
		ItemStack stack = new ItemStack(Config.recipe.getID(), 1, 0);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setInteger(TAG_ITEM_ID, itemID);
		tag.setBoolean(InventoryShop.TAG_IS_SAMPLE, isSample);
		return stack;
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		/*NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			return "Unknown recipe";
		}
		Integer itemID = tag.getInteger(TAG_ITEM_ID);
		if (itemID == null) {
			return "Unknown recipe";
		}*/
		int itemID = getItemID(stack);
		Item item = Item.itemsList[itemID];
		if (!(item instanceof Dota2Item)) {
			return "Corrupted recipe";
		}
		return "Recipe: " + ((Dota2Item) item).getItemDisplayName(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean showAdvanced) {
		int itemID = getItemID(stack);
		Item item = Item.itemsList[itemID];
		if (item instanceof Dota2Item) {
			((Dota2Item)item).addInformation(stack, player, list, showAdvanced);
		}
	}
	
	public static int getPrice(ItemStack stack) {
		int itemID = getItemID(stack);
		Item item = Item.itemsList[itemID];
		if (item instanceof Dota2Item) {
			return ((Dota2Item)item).getRecipePrice();
		}
		return 0;
	}
	
	public static int getItemID(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			Integer itemID = tag.getInteger(TAG_ITEM_ID);
			if (itemID != null) {
				return itemID.intValue();
			}
		}
		return 0;
	}
	
	@Override
	public int getItemDamageFromStack(ItemStack stack) {
		return getItemID(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack) {
		return ClientProxy.fontRWithIcons;
	}
	
	public static ItemStack copy(ItemStack stack, boolean isSample) {
		return forItem(getItemID(stack), isSample);
	}
}
