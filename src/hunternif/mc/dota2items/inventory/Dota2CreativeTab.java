package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Dota2CreativeTab extends CreativeTabs {
	
	public Dota2CreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack((Item)Config.dota2Logo.instance);
	}
}
