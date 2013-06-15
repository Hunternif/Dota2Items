package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GoldCoin extends Item {
	public static final String NAME = "goldCoin"; 

	public GoldCoin(int id) {
		super(id);
		setMaxStackSize(10);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Dota2Items.ID + ":coin");
	}
	
	/*@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
	}*/
}
