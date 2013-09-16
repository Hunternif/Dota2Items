package hunternif.mc.dota2items.config;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.ItemRecipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ConfigLoader {
	/** @param idsConfig Forge configuration file which holds Block and Item IDs.
	 * @param config a class containing static fields with Blocks and Items. */
	public static void preLoad(Configuration idConfig, Class config) {
		try {
			idConfig.load();
			Field[] fields = config.getFields();
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo<?> info = (CfgInfo)field.get(null);
					info.initialize(field);
					int id = info.id;
					if (info.isBlock()) {
						id = idConfig.getBlock(field.getName(), id).getInt();
					} else {
						id = idConfig.getItem(field.getName(), id).getInt();
					}
					info.id = id;
				}
			}
		} catch(Exception e) {
			Dota2Items.logger.severe("Failed to load config: " + e.toString());
		} finally {
			idConfig.save();
		}
	}
	
	public static void load(Class config) {
		try {
			List<CfgInfo> itemsWithRecipes = new ArrayList<CfgInfo>();
			Field[] fields = config.getFields();
			// Parse fields to instantiate the items:
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo info = (CfgInfo)field.get(null);
					Constructor constructor = info.type.getConstructor(int.class);
					info.instance = constructor.newInstance(info.id);
					if (info.isBlock()) {
						((Block)info.instance).setUnlocalizedName(field.getName());
						GameRegistry.registerBlock((Block)info.instance, ItemBlock.class, field.getName(), Dota2Items.ID);
						LanguageRegistry.addName(info.instance, info.name);
					} else {
						((Item)info.instance).setUnlocalizedName(field.getName());
						LanguageRegistry.addName(info.instance, info.name);
						GameRegistry.registerItem((Item)info.instance, field.getName(), Dota2Items.ID);
						Dota2Items.itemList.add((Item)info.instance);
						
						// Set Dota 2 Item attributes:
						if (info.instance instanceof Dota2Item) {
							Dota2Item item = (Dota2Item) info.instance;
							item.setPrice(info.price).setWeaponDamage(info.weaponDamage)
								.setPassiveBuff(info.passiveBuff).setShopColumn(info.column)
								.setDropsOnDeath(info.dropsOnDeath).setShopColumn(info.column);
							if (info.recipe != null && !info.recipe.isEmpty()) {
								itemsWithRecipes.add(info);
							}
							if (info.isFull3D) {
								item.setFull3D();
							}
						}
					}
					Dota2Items.logger.info("Registered item " + info.name);
				}
			}
			// Parse fields one more time to set their recipes:
			for (CfgInfo<? extends Dota2Item> info : itemsWithRecipes) {
				List<Dota2Item> recipeForShop = new ArrayList<Dota2Item>();
				List<ItemStack> recipeForCraft = new ArrayList<ItemStack>();
				
				for (CfgInfo<? extends Dota2Item> ingredient : info.recipe) {
					Dota2Item item = ingredient.instance;
					recipeForShop.add(item);
					recipeForCraft.add(new ItemStack(item, item.getDefaultQuantity()));
					ingredient.usedInRecipes.add(info.instance);
				}
				info.instance.setRecipe(recipeForShop);
				
				if (info.instance.isRecipeItemRequired()) {
					recipeForCraft.add(ItemRecipe.forItem(info.getID(), false));
				}
				ItemStack craftResult = new ItemStack(info.instance, info.instance.getDefaultQuantity());
				GameRegistry.addShapelessRecipe(craftResult, recipeForCraft.toArray());
				
				Dota2Items.logger.info("Added recipe for Dota 2 item " + info.name);
			}
		} catch(Exception e) {
			Dota2Items.logger.severe("Failed to instantiate items: " + e.toString());
		}
	}
}