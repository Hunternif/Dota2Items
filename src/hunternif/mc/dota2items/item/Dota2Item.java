package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.client.gui.ColorHelper;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.inventory.InventoryShop;
import hunternif.mc.dota2items.inventory.ItemColumn;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Dota2Item extends Item {
	public boolean dropsOnDeath = false;
	public Buff passiveBuff;
	public ItemColumn shopColumn;
	public int defaultQuantity = 1;
	
	public static int maxTooltipWidth = 128;
	public List<String> descriptionLines;
	
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
	
	public static void playDenyGeneralSound(EntityPlayer player) {
		if (player.worldObj.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(Sound.DENY_GENERAL.name, 1.0F, 1.0F);
		}
	}

	/**
	 * Returns the Sound that signifies the particular reason why the player
	 * cannot use this item.
	 */
	public Sound canUseItem(ItemStack stack, EntityLiving player, Entity target) {
		if (target instanceof EntityShopkeeper) { // Not even in Creative mode.
			return Sound.MAGIC_IMMUNE;
		}
		if (player instanceof EntityPlayer && ((EntityPlayer)player).capabilities.isCreativeMode) {
			return null;
		}
		EntityStats playerStats = Dota2Items.mechanics.getEntityStats(player);
		if (!playerStats.canUseItems()) {
			return Sound.DENY_SILENCE;
		}
		if (target != null) {
			if (!(target instanceof EntityLiving)) {
				return Sound.DENY_GENERAL;
			}
			EntityStats targetStats = Dota2Items.mechanics.getEntityStats((EntityLiving)target);
			if (targetStats.isMagicImmune()) {
				return Sound.MAGIC_IMMUNE;
			}
		}
		return null;
	}
	/** See {@link #canUseItem(ItemStack, EntityLiving, Entity)} */
	public Sound canUseItem(ItemStack stack, EntityLiving player) {
		return canUseItem(stack, player, null);
	}
	
	/**
	 * If the player cannot use this item, a respective sound is played.
	 * @return whether the player can use this item.
	 */
	public boolean tryUse(ItemStack stack, EntityLiving player, Entity target) {
		Sound failSound = canUseItem(stack, player, target);
		if (failSound != null && player.worldObj.isRemote) {
			Minecraft.getMinecraft().sndManager.playSoundFX(failSound.name, 1.0F, 1.0F);
		}
		return failSound == null;
	}
	/** See {@link #tryUse(ItemStack, EntityLiving, Entity)} */
	public boolean tryUse(ItemStack stack, EntityLiving player) {
		return tryUse(stack, player, null);
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
		boolean isRecipe = stack.itemID == Config.recipe.getID();
		int itemPrice = isRecipe ? this.getRecipePrice() : this.getTotalPrice();
		list.add(ClientProxy.ICON_GOLD.key + EnumChatFormatting.GOLD + itemPrice*stack.stackSize);
		
		if (descriptionLines != null) {
			list.addAll(descriptionLines);
		}
		
		if (this instanceof CooldownItem) {
			float cooldown = ((CooldownItem)this).getCooldown();
			String cooldownStr;
			if (cooldown != MathHelper.floor_float(cooldown)) {
				cooldownStr = String.format("%.1f", cooldown);
			} else {
				cooldownStr = String.format("%.0f", cooldown);
			}
			if (((CooldownItem)this).getManaCost() > 0) {
				cooldownStr += "     " + ClientProxy.ICON_MANACOST.key + EnumChatFormatting.GRAY + ((CooldownItem)this).getManaCost();
			}
			list.add(ClientProxy.ICON_COOLDOWN.key + EnumChatFormatting.GRAY + cooldownStr);
		}
		// If the item is displayed in shop
		if (isSampleItemStack(stack)) {
			if (Dota2Items.mechanics.getEntityStats(player).getGold() < itemPrice*stack.stackSize) {
				list.add(EnumChatFormatting.DARK_RED + "Not enough gold");
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack) {
		return ClientProxy.fontRWithIcons;
	}
	
	public static boolean isSampleItemStack(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		return tag != null && tag.getBoolean(InventoryShop.TAG_IS_SAMPLE);
	}
	
	public static boolean canBuy(ItemStack itemStack, EntityPlayer player) {
		int curPrice = 0;
		if (itemStack.getItem() instanceof Dota2Item) {
			curPrice = ((Dota2Item)itemStack.getItem()).getTotalPrice() * itemStack.stackSize;
		} else if (itemStack.getItem() instanceof ItemRecipe) {
			curPrice = ItemRecipe.getPrice(itemStack);
		}
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		return stats.getGold() >= curPrice;
	}
	public static boolean hasRecipe(ItemStack itemStack) {
		return itemStack.getItem() instanceof Dota2Item &&
				((Dota2Item)itemStack.getItem()).hasRecipe();
	}
	
	public static int getPrice(ItemStack itemStack) {
		if (itemStack == null) {
			return 0;
		}
		if (itemStack.getItem() instanceof Dota2Item) {
			Dota2Item item = (Dota2Item) itemStack.getItem(); 
			return item.getTotalPrice() * itemStack.stackSize;
		} else if (itemStack.getItem() instanceof ItemRecipe) {
			return ItemRecipe.getPrice(itemStack) * itemStack.stackSize;
		} else {
			return 0;
		}
	}
	public static int getSellPrice(ItemStack itemStack) {
		return getPrice(itemStack)/2;
	}
}
