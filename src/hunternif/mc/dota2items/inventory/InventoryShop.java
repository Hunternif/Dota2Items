package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryShop implements IInventory {
	public static final String TAG_IS_SAMPLE = "D2IisSample";
	
	public static final int SLOT_RESULT = 999;
	public static final int SLOT_RECIPE_RESULT = 1000;
	public static final int SLOT_INGR_1 = 1001;
	public static final int SLOT_INGR_2 = 1002;
	public static final int SLOT_INGR_3 = 1003;
	public static final int SLOT_INGR_4 = 1004;
	
	private static final int COLUMNS = 11;
	private static final int ROWS = 12;
	
	private static ItemStack[][] itemSamples = new ItemStack[COLUMNS][ROWS];
	
	public static void populate() {
		populateColumn(
				ItemColumn.COLUMN_CONSUMABLES,
//				Dota2Items.clarity,
				Dota2Items.tango
//				Dota2Items.healingSalve,
//				Dota2Items.smokeOfDeceit,
//				Dota2Items.townPortal,
//				Dota2Items.dustOfAppearance,
//				Dota2Items.animalCourier,
//				Dota2Items.flyingCourier,
//				Dota2Items.observerWard,
//				Dota2Items.sentryWard,
//				Dota2Items.bottle
		);
//		populateColumn(
//				ItemColumn.COLUMN_ATTRIBUTES,
//				Dota2Items.ironBranch,
//				Dota2Items.gauntletsOfStrength,
//				Dota2Items.slippersOfAgility,
//				Dota2Items.mantleOfIntelligence,
//				Dota2Items.circlet,
//				Dota2Items.beltOfStrength,
//				Dota2Items.bandOfElvenskin,
//				Dota2Items.robeOfTheMagi,
//				Dota2Items.ogreClub,
//				Dota2Items.bladeOfAlacrity,
//				Dota2Items.staffOfWizardry,
//				Dota2Items.ultimateOrb
//		);
		populateColumn(
				ItemColumn.COLUMN_ARMAMENTS,
				Dota2Items.ringOfProtection,
				Dota2Items.quellingBlade
//				Dota2Items.stoutShield,
//				Dota2Items.bladesOfAttack,
//				Dota2Items.chainmail,
//				Dota2Items.helmOfIronWill,
//				Dota2Items.broadsword,
//				Dota2Items.quarterstaff,
//				Dota2Items.claymore,
//				Dota2Items.javelin,
//				Dota2Items.platemail,
//				Dota2Items.mithrilHammer
		);
		
		populateColumn(
				ItemColumn.COLUMN_ARCANE,
//				Dota2Items.magicStick,
//				Dota2Items.sagesMask,
//				Dota2Items.ringOfRegen,
				Dota2Items.bootsOfSpeed,
//				Dota2Items.glovesOfHaste,
//				Dota2Items.cloak,
//				Dota2Items.gemOfTrueSight,
//				Dota2Items.morbidMask,
//				Dota2Items.ghostScepter,
//				Dota2Items.talismanOfEvasion,
				Dota2Items.blinkDagger
//				Dota2Items.shadowAmulet
		);
//		populateColumn(
//				ItemColumn.COLUMN_COMMON,
//				Dota2Items.wraithBand,
//				Dota2Items.nullTalisman,
//				Dota2Items.magicWand,
//				Dota2Items.bracer,
//				Dota2Items.poorMansShield,
//				Dota2Items.soulRing,
//				Dota2Items.phaseBoots,
//				Dota2Items.powerTreads,
//				Dota2Items.oblivionStaff,
//				Dota2Items.perserverance,
//				Dota2Items.handOfMidas,
//				Dota2Items.bootsOfTravel
//		);
//		populateColumn(
//				ItemColumn.COLUMN_SUPPORT,
//				Dota2Items.ringOfBasilius,
//				Dota2Items.headdress,
//				Dota2Items.buckler,
//				Dota2Items.urnOfShadows,
//				Dota2Items.ringOfAquila,
//				Dota2Items.tranquilBoots,
//				Dota2Items.medallionOfCourage,
//				Dota2Items.arcaneBoots,
//				Dota2Items.drumOfEndurance,
//				Dota2Items.vladmirsOffering,
//				Dota2Items.mekansm,
//				Dota2Items.pipeOfInsight
//		);
		populateColumn(
				ItemColumn.COLUMN_CASTER,
				//Dota2Items.forceStaff,
//				Dota2Items.necronomicon,
				Dota2Items.eulsScepter
//				Dota2Items.dagon,
//				Dota2Items.veilOfDiscord,
//				Dota2Items.rodOfAtos,
//				Dota2Items.AghanimsScepter,
//				Dota2Items.orchidMalevolence,
//				Dota2Items.refresherOrb,
//				Dota2Items.scytheOfVyse
		);
//		populateColumn(
//				ItemColumn.COLUMN_WEAPONS,
//				Dota2Items.crystalys,
//				Dota2Items.armletOfMordiggian,
//				Dota2Items.skullBasher,
//				Dota2Items.shadowBlade,
//				Dota2Items.battleFury,
//				Dota2Items.etherealBlade,
//				Dota2Items.radiance,
//				Dota2Items.monkeyKingBar,
//				Dota2Items.daedalus,
//				Dota2Items.butterfly,
//				Dota2Items.divineRapier,
//				Dota2Items.abyssalBlade
//		);
//		populateColumn(
//				ItemColumn.COLUMN_ARMOR,
//				Dota2Items.hoodOfDefiance,
//				Dota2Items.bladeMail,
//				Dota2Items.vanguard,
//				Dota2Items.soulBooster,
//				Dota2Items.blackKingBar,
//				Dota2Items.shivasGuard,
//				Dota2Items.mantaStyle,
//				Dota2Items.bloodstone,
//				Dota2Items.linkensSphere,
//				Dota2Items.assaultCuirass,
//				Dota2Items.heartOfTarrasque
//		);
//		populateColumn(
//				ItemColumn.COLUMN_ARTIFACTS,
//				Dota2Items.helmOfTheDominator,
//				Dota2Items.maskOfMadness,
//				Dota2Items.sange,
//				Dota2Items.yasha,
//				Dota2Items.maelstrom,
//				Dota2Items.diffusalBlade,
//				Dota2Items.desolator,
//				Dota2Items.heavensHalberd,
//				Dota2Items.sangeAndYasha,
//				Dota2Items.mjollnir,
//				Dota2Items.eyeOfSkadi,
//				Dota2Items.satanic
//		);
//		populateColumn(
//				ItemColumn.COLUMN_SECRET_SHOP,
//				Dota2Items.demonEdge,
//				Dota2Items.eaglesong,
//				Dota2Items.reaver,
//				Dota2Items.sacredRelic,
//				Dota2Items.hyperStone,
//				Dota2Items.ringOfHealth,
//				Dota2Items.voidStone,
//				Dota2Items.mysticStaff,
//				Dota2Items.energyBooster,
//				Dota2Items.pointBooster,
//				Dota2Items.vitalityBooster,
//				Dota2Items.orbOfVenom
//		);
	}
	private static void populateColumn(ItemColumn column, Dota2Item... items) {
		for (int i = 0; i < items.length; i++) {
			items[i].shopColumn = column;
			ItemStack stack = new ItemStack(items[i]);
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null) {
				tag = new NBTTagCompound();
				stack.setTagCompound(tag);
			}
			tag.setBoolean(TAG_IS_SAMPLE, true);
			itemSamples[column.id][i] = stack;
		}
	}
	
	public static InventoryShop newRegularShop() {
		return new InventoryShop(new int[]{
			ItemColumn.COLUMN_CONSUMABLES.id,
			ItemColumn.COLUMN_ATTRIBUTES.id,
			ItemColumn.COLUMN_ARMAMENTS.id,
			ItemColumn.COLUMN_ARCANE.id,
			ItemColumn.COLUMN_COMMON.id,
			ItemColumn.COLUMN_SUPPORT.id,
			ItemColumn.COLUMN_CASTER.id,
			ItemColumn.COLUMN_WEAPONS.id,
			ItemColumn.COLUMN_ARMOR.id,
			ItemColumn.COLUMN_ARTIFACTS.id
		}, 5);
	}
	
	private int[] columns;
	private int height;
	private int scrollPos;
	private String filterStr;
	public InventoryShop(int[] columns, int height) {
		this.columns = columns;
		this.height = height;
		this.scrollPos = 0;
		this.filterStr = "";
	}
	public void setFilterStr(String value) {
		if (!this.filterStr.equals(value.toLowerCase())) {
			this.filterStr = value.toLowerCase();
			onInventoryChanged();
		}
	}
	public void setScrollPos(int value) {
		if (this.scrollPos != value) {
			this.scrollPos = value;
			onInventoryChanged();
		}
	}

	@Override
	public int getSizeInventory() {
		return columns.length*height;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		int xPos = i % columns.length;
		int yPos = (i - xPos)/columns.length;
		int columnIndex = columns[xPos];
		ItemStack[] column = itemSamples[columnIndex];
		List<ItemStack> filteredColumn = new ArrayList<ItemStack>();
		for (int j = 0; j < ROWS; j++) {
			ItemStack stack = column[j];
			if (stack == null) {
				continue;
			}
			String name = stack.getDisplayName();
			if (name.toLowerCase().contains(filterStr)) {
				filteredColumn.add(stack);
			}
		}
		if (filteredColumn.size() < yPos + scrollPos + 1) {
			return null;
		} else {
			return filteredColumn.get(yPos + scrollPos);
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {}

	@Override
	public String getInvName() {
		return "Dota 2 Shop Inventory";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

}
