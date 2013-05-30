package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Dummy item, just an icon in the creative tab. */
public class Dota2Logo extends Item {
	public static final String NAME = "dota2Logo";
	
	public Dota2Logo(int id) {
		super(id);
		setUnlocalizedName(NAME);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Dota2Items.ID + ":logo");
	}

}
