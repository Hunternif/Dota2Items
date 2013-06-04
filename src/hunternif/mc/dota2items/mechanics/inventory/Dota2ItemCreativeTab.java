package hunternif.mc.dota2items.mechanics.inventory;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class Dota2ItemCreativeTab extends CreativeTabs {
	
	public Dota2ItemCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Dota2Items.dota2Logo);
	}
}
