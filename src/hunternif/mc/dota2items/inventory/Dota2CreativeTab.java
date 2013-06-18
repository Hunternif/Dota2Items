package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class Dota2CreativeTab extends CreativeTabs {
	
	public Dota2CreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Dota2Items.dota2Logo);
	}
}
