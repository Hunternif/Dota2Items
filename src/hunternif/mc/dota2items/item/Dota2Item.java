package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Dota2Item extends Item {
	public boolean dropsOnDeath = false;
	public Buff passiveBuff;
	
	
	public Dota2Item(int id) {
		super(id);
		setCreativeTab(Dota2Items.dota2CreativeTab);
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Dota2Items.ID + ":" + getUnlocalizedName().substring("item.".length()));
	}
	
	public static void playDenyGeneralSound(World world) {
		if (world.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Dota2ItemSounds.DENY_GENERAL, 1.0F, 1.0F);
		}
	}
	
	public static void playMagicImmuneSound(World world) {
		if (world.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Dota2ItemSounds.MAGIC_IMMUNE, 1.0F, 1.0F);
		}
	}
	
	public boolean canUseItem(Entity player) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		if (stats == null) {
			return true;
		} else {
			return stats.canUseItems();
		}
	}
}
