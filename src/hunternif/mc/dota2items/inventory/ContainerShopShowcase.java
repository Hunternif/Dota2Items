package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.ItemRecipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerShopShowcase extends ContainerShopBuy {
	private List<ItemStack> recipeResults = new ArrayList<ItemStack>();
	private List<ItemStack> recipeIngredients = new ArrayList<ItemStack>();
	
	public ContainerShopShowcase(InventoryPlayer inventoryPlayer) {
		super(inventoryPlayer);
	}
	
	@Override
	public ItemStack slotClick(int slotNumber, int mouseClick, int holdShift, EntityPlayer player) {
		if (slotNumber != slotResultNumber && slotNumber >= 0) {
			Slot slot = (Slot)this.inventorySlots.get(slotNumber);
			if (slot.inventory instanceof InventoryShop) {
				ItemStack stack = invShop.getStackInSlot(slotNumber);
				if (stack != null) {
					Dota2Item item = (Dota2Item) stack.getItem();
					if (item.hasRecipe()) {
						setRecipeResult(item);
					} else {
						setRecipeIngredient(item);
					}
					EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
					if (stats.getGold() >= item.getTotalPrice()) {
						setResultItem(item);
					} else {
						setResultItem((Dota2Item)null);
					}
				} else {
					setRecipeResult(null);
					setResultItem((Dota2Item)null);
				}
				return null;
			}
		}
		return super.slotClick(slotNumber, mouseClick, holdShift, player);
	}
	
	public List<ItemStack> getRecipeResults() {
		return recipeResults;
	}
	public List<ItemStack> getRecipeIngredients() {
		return recipeIngredients;
	}
	public void setRecipeResult(Dota2Item item) {
		recipeResults.clear();
		recipeIngredients.clear();
		if (item != null) {
			recipeResults.add(invShop.sampleFor(item));
			if (item.hasRecipe()) {
				for (Dota2Item curRecipeItem : item.getRecipe()) {
					recipeIngredients.add(invShop.sampleFor(curRecipeItem));
				}
				if (item.isRecipeItemRequired()) {
					recipeIngredients.add(ItemRecipe.forItem(item, true));
				}
			}
		}
	}
	/** Sets this 1 item as ingredient and shows all recipes it is used in. */
	public void setRecipeIngredient(Dota2Item item) {
		recipeResults.clear();
		recipeIngredients.clear();
		if (item != null) {
			recipeIngredients.add(invShop.sampleFor(item));
			int recipesCount = item.getUsedInRecipes().size();
			if (recipesCount > 0) {
				if (recipesCount == 1) {
					// If only used in 1 recipe, then show the full recipe:
					Dota2Item recipeResult = (Dota2Item)item.getUsedInRecipes().toArray()[0];
					setRecipeResult(recipeResult);
				} else {
					for (Dota2Item curRecipeItem : item.getUsedInRecipes()) {
						recipeResults.add(invShop.sampleFor(curRecipeItem));
					}
				}
			}
		}
	}
}
