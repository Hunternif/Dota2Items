package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.client.gui.ColorHelper;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.inventory.InventoryShop;
import hunternif.mc.dota2items.inventory.ItemColumn;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Dota2Item extends Item {
	public boolean dropsOnDeath = false;
	public Buff passiveBuff;
	public ItemColumn shopColumn;
	
	/** If this item has a recipe, this "price" represents the price of the recipe. */
	private int price;
	private List<Dota2Item> recipe;
	
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
			Minecraft.getMinecraft().sndManager.playSoundFX(Sound.DENY_GENERAL.name, 1.0F, 1.0F);
		}
	}
	
	public static void playMagicImmuneSound(World world) {
		if (world.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Sound.MAGIC_IMMUNE.name, 1.0F, 1.0F);
		}
	}
	
	public boolean canUseItem(EntityLiving player) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		if (stats == null) {
			return true;
		} else {
			return stats.canUseItems();
		}
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	public void setRecipePrice(int price) {
		setPrice(price);
	}
	public void setRecipe(List<Dota2Item> list) {
		this.recipe = list;
	}
	public void setRecipe(List<Dota2Item> list, int priceOfRecipe) {
		setRecipe(list);
		setRecipePrice(priceOfRecipe);
	}
	public boolean hasRecipe() {
		return recipe != null && !recipe.isEmpty();
	}
	public boolean isRecipeItemRequired() {
		return hasRecipe() && price > 0;
	}
	public List<Dota2Item> getRecipe() {
		return recipe;
	}
	public int getRecipePrice() {
		return price;
	}
	public int getTotalPrice() {
		int totalPrice = getRecipePrice();
		if (hasRecipe()) {
			for (Dota2Item dota2Item : recipe) {
				totalPrice += dota2Item.getTotalPrice();
			}
		}
		return totalPrice;
	}
	public boolean isSecretShopRequired() {
		if (this.shopColumn == ItemColumn.COLUMN_SECRET_SHOP) {
			return true;
		}
		if (hasRecipe()) {
			for (Dota2Item dota2Item : recipe) {
				if (dota2Item.isSecretShopRequired()) {
					return true;
				}
			}
		}
		return false;
	}
	public int getSellPrice() {
		return getTotalPrice()/2;
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		return ColorHelper.prefixForColumn(shopColumn) + super.getItemDisplayName(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean showAdvanced) {
		list.add(ClientProxy.ICON_GOLD.key + EnumChatFormatting.GOLD + this.getTotalPrice()*stack.stackSize);
		if (this instanceof CooldownItem) {
			float cooldown = ((CooldownItem)this).getCooldown();
			String cooldownStr;
			if (cooldown != MathHelper.floor_float(cooldown)) {
				cooldownStr = String.format("%.1f", cooldown);
			} else {
				cooldownStr = String.format("%.0f", cooldown);
			}
			list.add(ClientProxy.ICON_COOLDOWN.key + EnumChatFormatting.GRAY + cooldownStr);
		}
		// If the item is displayed in shop
		if (isSampleItemStack(stack)) {
			if (Dota2Items.mechanics.getEntityStats(player).getGold() < this.getTotalPrice()*stack.stackSize) {
				list.add(EnumChatFormatting.DARK_RED + "Not enough gold");
			}
			if (isSecretShopRequired()) {
				list.add(EnumChatFormatting.DARK_RED + "Requires secret shop");
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack) {
		return ClientProxy.fontRenderer;
	}
	
	public static boolean isSampleItemStack(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		return tag != null && tag.getBoolean(InventoryShop.TAG_IS_SAMPLE);
	}
}
