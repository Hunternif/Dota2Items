package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GoldCoin extends Item {

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
