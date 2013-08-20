package hunternif.mc.dota2items;

import hunternif.mc.dota2items.block.BlockCycloneContainer;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.inventory.ItemColumn;
import hunternif.mc.dota2items.item.BandOfElvenskin;
import hunternif.mc.dota2items.item.BeltOfStrength;
import hunternif.mc.dota2items.item.BladeOfAlacrity;
import hunternif.mc.dota2items.item.BladesOfAttack;
import hunternif.mc.dota2items.item.BlinkDagger;
import hunternif.mc.dota2items.item.Bracer;
import hunternif.mc.dota2items.item.Broadsword;
import hunternif.mc.dota2items.item.Butterfly;
import hunternif.mc.dota2items.item.Chainmail;
import hunternif.mc.dota2items.item.Circlet;
import hunternif.mc.dota2items.item.Claymore;
import hunternif.mc.dota2items.item.Cloak;
import hunternif.mc.dota2items.item.DemonEdge;
import hunternif.mc.dota2items.item.DivineRapier;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.Dota2Logo;
import hunternif.mc.dota2items.item.Eaglesong;
import hunternif.mc.dota2items.item.EnergyBooster;
import hunternif.mc.dota2items.item.EulsScepter;
import hunternif.mc.dota2items.item.GauntletsOfStrength;
import hunternif.mc.dota2items.item.GlovesOfHaste;
import hunternif.mc.dota2items.item.GoldCoin;
import hunternif.mc.dota2items.item.HelmOfIronWill;
import hunternif.mc.dota2items.item.Hyperstone;
import hunternif.mc.dota2items.item.IronBranch;
import hunternif.mc.dota2items.item.ItemRecipe;
import hunternif.mc.dota2items.item.MantleOfIntelligence;
import hunternif.mc.dota2items.item.MithrilHammer;
import hunternif.mc.dota2items.item.MysticStaff;
import hunternif.mc.dota2items.item.NullTalisman;
import hunternif.mc.dota2items.item.OblivionStaff;
import hunternif.mc.dota2items.item.OgreClub;
import hunternif.mc.dota2items.item.Perseverance;
import hunternif.mc.dota2items.item.Platemail;
import hunternif.mc.dota2items.item.PointBooster;
import hunternif.mc.dota2items.item.Quarterstaff;
import hunternif.mc.dota2items.item.QuellingBlade;
import hunternif.mc.dota2items.item.Reaver;
import hunternif.mc.dota2items.item.RingOfHealth;
import hunternif.mc.dota2items.item.RingOfRegen;
import hunternif.mc.dota2items.item.RobeOfTheMagi;
import hunternif.mc.dota2items.item.SacredRelic;
import hunternif.mc.dota2items.item.SagesMask;
import hunternif.mc.dota2items.item.SlippersOfAgility;
import hunternif.mc.dota2items.item.SoulBooster;
import hunternif.mc.dota2items.item.StaffOfWizardry;
import hunternif.mc.dota2items.item.StoutShield;
import hunternif.mc.dota2items.item.TalismanOfEvasion;
import hunternif.mc.dota2items.item.Tango;
import hunternif.mc.dota2items.item.UltimateOrb;
import hunternif.mc.dota2items.item.Vanguard;
import hunternif.mc.dota2items.item.VitalityBooster;
import hunternif.mc.dota2items.item.WraithBand;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Config {
	// TODO generalize Config for any mod items and blocks, then extend it for Dota2Items.
	public static void preLoad(Configuration config) {
		try {
			config.load();
			Field[] fields = Config.class.getFields();
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo<?> info = (CfgInfo)field.get(null);
					int id = info.id;
					if (info.isBlock()) {
						id = config.getBlock(field.getName(), id).getInt();
					} else {
						id = config.getItem(field.getName(), id).getInt();
					}
					info.id = id;
				}
			}
		} catch(Exception e) {
			FMLLog.log(Dota2Items.ID, Level.SEVERE, "Failed to load config: " + e.toString());
		} finally {
			config.save();
		}
	}
	
	public static void load() {
		try {
			List<CfgInfo> itemsWithRecipes = new ArrayList<>();
			Field[] fields = Config.class.getFields();
			// Parse fields to instantiate the items:
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo info = (CfgInfo)field.get(null);
					//Unchecked conversion
					info.instance = info.getType().getConstructor(int.class).newInstance(info.id);
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
								.setPassiveBuff(info.passiveBuff).setShopColumn(info.shopColumn);
							if (info.recipe != null && !info.recipe.isEmpty()) {
								itemsWithRecipes.add(info);
							}
						}
					}
				}
			}
			// Parse fields one more time to set their recipes:
			for (CfgInfo<?> info : itemsWithRecipes) {
				List<Dota2Item> recipeForShop = new ArrayList<>();
				List<ItemStack> recipeForCraft = new ArrayList<>();
				
				for (CfgInfo<?> ingredient : info.recipe) {
					Dota2Item item = (Dota2Item) ingredient.instance;
					recipeForShop.add(item);
					recipeForCraft.add(new ItemStack(item, item.getDefaultQuantity()));
				}
				((Dota2Item) info.instance).setRecipe(recipeForShop);
				
				if (((Dota2Item)info.instance).isRecipeItemRequired()) {
					recipeForCraft.add(ItemRecipe.forItem(info.getID(), false));
				}
				ItemStack craftResult = new ItemStack((Dota2Item)info.instance, ((Dota2Item)info.instance).getDefaultQuantity());
				GameRegistry.addShapelessRecipe(craftResult, recipeForCraft.toArray());
				
				FMLLog.log(Dota2Items.ID, Level.INFO, "Added recipe for Dota 2 item " + info.name);
			}
		} catch(Exception e) {
			FMLLog.log(Dota2Items.ID, Level.SEVERE, "Failed to instantiate items: " + e.toString());
		}
	}
	
	// Items
	public static CfgInfo<Dota2Logo> dota2Logo = new CfgInfo (27000, "Dota 2 Logo");
	public static CfgInfo<BlinkDagger> blinkDagger = new CfgInfo<>(27001, "Blink Dagger")
			.setPrice(2150).setWeaponDamage(4);
	public static CfgInfo<Tango> tango = new CfgInfo<>(27002, "Tango").setPrice(30);
	public static CfgInfo<QuellingBlade> quellingBlade = new CfgInfo<>(27003, "Quelling Blade")
			.setPrice(225).setWeaponDamage(6)
			.setPassiveBuff(new Buff("Quelling Blade").setDamagePercent(32, 16).setDoesNotStack());
	public static CfgInfo<EulsScepter> eulsScepter = new CfgInfo<>(27004, "Eul's Scepter of Divinity")
			.setPrice(600).setWeaponDamage(2).setShopColumn(ItemColumn.COLUMN_CASTER)
			.setPassiveBuff( new Buff("Eul's Scepter of Divinity").setMovementSpeed(30).setIntelligence(10).setManaRegenPercent(150) )
			.setRecipe(staffOfWizardry, sagesMask, voidStone);
	public static CfgInfo<Dota2Item> ringOfProtection = new CfgInfo<>(27005, "Ring of Protection").setPrice(175).setPassiveBuff(new Buff("Ring of Protection").setArmor(2));
	public static CfgInfo<Dota2Item> bootsOfSpeed = new CfgInfo<>(27006, "Boots of Speed").setPrice(450).setPassiveBuff(new Buff("Boots of Speed").setMovementSpeed(50));
	public static CfgInfo<GoldCoin> goldCoin = new CfgInfo<>(27007, "Gold Coin");
	public static CfgInfo<Dota2Item> voidStone = new CfgInfo<>(27008, "Void Stone")
			.setPrice(875).setPassiveBuff(new Buff("Void Stone").setManaRegenPercent(100));
	public static CfgInfo<SagesMask> sagesMask = new CfgInfo<>(27009, "Sage's Mask");
	public static CfgInfo<StaffOfWizardry> staffOfWizardry = new CfgInfo<>(27010, "Staff of Wizardry");
	public static CfgInfo<ItemRecipe> recipe = new CfgInfo<>(27011, "Recipe");
	public static CfgInfo<IronBranch> ironBranch = new CfgInfo<>(27012, "Iron Branch");
	public static CfgInfo<GauntletsOfStrength> gauntletsOfStrength = new CfgInfo<>(27013, "Gauntlets of Strength");
	public static CfgInfo<SlippersOfAgility> slippersOfAgility = new CfgInfo<>(27014, "Slippers of Agility");
	public static CfgInfo<MantleOfIntelligence> mantleOfIntelligence =	new CfgInfo<>(27015, "Mantle of Intelligence");
	public static CfgInfo<Circlet> circlet = new CfgInfo<>(27016, "Circlet");
	public static CfgInfo<BeltOfStrength> beltOfStrength = new CfgInfo<>(27017, "Belt of Strength");
	public static CfgInfo<BandOfElvenskin> bandOfElvenskin = new CfgInfo<>(27018, "Band of Elvenskin");
	public static CfgInfo<RobeOfTheMagi> robeOfTheMagi = new CfgInfo<>(27019, "Robe of the Magi");
	public static CfgInfo<OgreClub> ogreClub = new CfgInfo<>(27020, "Ogre Club");
	public static CfgInfo<BladeOfAlacrity> bladeOfAlacrity = new CfgInfo<>(27021, "Blade of Alacrity");
	public static CfgInfo<UltimateOrb> ultimateOrb = new CfgInfo<>(27022, "Ultimate Orb");
	public static CfgInfo<BladesOfAttack> bladesOfAttack = new CfgInfo<>(27023, "Blades of Attack");
	public static CfgInfo<Chainmail> chainmail = new CfgInfo<>(27024, "Chainmail");
	public static CfgInfo<HelmOfIronWill> helmOfIronWill = new CfgInfo<>(27025, "Helm of Iron Will");
	public static CfgInfo<Broadsword> broadsword = new CfgInfo<>(27026, "Broadsword");
	public static CfgInfo<Quarterstaff> quarterstaff = new CfgInfo<>(27027, "Quarterstaff");
	public static CfgInfo<Claymore> claymore = new CfgInfo<>(27028, "Claymore");
	public static CfgInfo<Platemail> platemail = new CfgInfo<>(27029, "Platemail");
	public static CfgInfo<MithrilHammer> mithrilHammer = new CfgInfo<>(27030, "Mithril Hammer");
	public static CfgInfo<StoutShield> stoutShield = new CfgInfo<>(27031, "Stout Shield");
	public static CfgInfo<RingOfRegen> ringOfRegen = new CfgInfo<>(27032, "Ring of Regen");
	public static CfgInfo<GlovesOfHaste> glovesOfHaste = new CfgInfo<>(27033, "Gloves of Haste");
	public static CfgInfo<Cloak> cloak = new CfgInfo<>(27034, "Cloak");
	public static CfgInfo<TalismanOfEvasion> talismanOfEvasion = new CfgInfo<>(27035, "Talisman of Evasion");
	public static CfgInfo<WraithBand> wraithBand = new CfgInfo<>(27036, "Wraith Band");
	public static CfgInfo<NullTalisman> nullTalisman = new CfgInfo<>(27037, "Null Talisman");
	public static CfgInfo<Bracer> bracer = new CfgInfo<>(27038, "Bracer");
	public static CfgInfo<RingOfHealth> ringOfHealth = new CfgInfo<>(27039, "Ring of Health");
	public static CfgInfo<Hyperstone> hyperstone = new CfgInfo<>(27040, "Hyperstone");
	public static CfgInfo<DemonEdge> demonEdge = new CfgInfo<>(27041, "Demon Edge");
	public static CfgInfo<SacredRelic> sacredRelic = new CfgInfo<>(27042, "Sacred Relic");
	public static CfgInfo<Reaver> reaver = new CfgInfo<>(27043, "Reaver");
	public static CfgInfo<Eaglesong> eaglesong = new CfgInfo<>(27044, "Eaglesong");
	public static CfgInfo<MysticStaff> mysticStaff = new CfgInfo<>(27045, "Mystic Staff");
	public static CfgInfo<VitalityBooster> vitalityBooster = new CfgInfo<>(27046, "Vitality Booster");
	public static CfgInfo<EnergyBooster> energyBooster = new CfgInfo<>(27047, "Energy Booster");
	public static CfgInfo<PointBooster> pointBooster = new CfgInfo<>(27048, "Point Booster");
	public static CfgInfo<SoulBooster> soulBooster = new CfgInfo<>(27049, "Soul Booster");
	public static CfgInfo<Perseverance> perseverance = new CfgInfo<>(27050, "Perseverance");
	public static CfgInfo<OblivionStaff> oblivionStaff = new CfgInfo<>(27051, "Oblivion Staff");
	public static CfgInfo<Vanguard> vanguard = new CfgInfo<>(27052, "Vanguard");
	public static CfgInfo<Butterfly> butterfly = new CfgInfo<>(27053, "Butterfly");
	public static CfgInfo<DivineRapier> divineRapier = new CfgInfo<>(27054, "Divine Rapier");
	
	// Blocks
	public static CfgInfo<BlockCycloneContainer> cycloneContainer = new CfgInfo<>(2700, "Cyclone Container");
	
	public static class CfgInfo<T> {
		public T instance;
		protected int id;
		public String name;
		protected int price;
		protected List<CfgInfo<?>> recipe;
		protected Buff passiveBuff;
		protected float weaponDamage;
		protected ItemColumn shopColumn;
		public CfgInfo(int defaultID, String englishName) {
			this.id = defaultID;
			this.name = englishName;
		}
		public int getID() {
			return isBlock() ? ((Block)instance).blockID : ((Item)instance).itemID;
		}
		public boolean isBlock() {
			return Block.class.isAssignableFrom(getType());
		}
		public CfgInfo setPrice(int value) {
			this.price = value;
			return this;
		}
		public CfgInfo setRecipe(CfgInfo<?> ... items) {
			this.recipe = Arrays.asList(items);
			return this;
		}
		public CfgInfo setPassiveBuff(Buff buff) {
			this.passiveBuff = buff;
			return this;
		}
		public CfgInfo setWeaponDamage(float value) {
			this.weaponDamage = value;
			return this;
		}
		public CfgInfo setShopColumn(ItemColumn column) {
			this.shopColumn = column;
			return this;
		}
		public Class<?> getType() {
			return this.getClass().getTypeParameters()[0].getClass();
		}
	}
}