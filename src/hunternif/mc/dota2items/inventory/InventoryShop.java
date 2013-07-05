package hunternif.mc.dota2items.inventory;

import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.Config.CfgInfo;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryShop implements IInventory {
	public static final String TAG_IS_SAMPLE = "D2IisSample";
	
	private static final int COLUMNS = 11;
	private static final int ROWS = 12;
	
	private static final ItemStack[][] itemSamples = new ItemStack[COLUMNS][ROWS];
	private static final Map<Class<?>, ItemStack> samplesMap = new HashMap<Class<?>, ItemStack>();
	
	public static void populate() {
		populateColumn(
				ItemColumn.COLUMN_CONSUMABLES,
//				Config.clarity,
				Config.tango
//				Config.healingSalve,
//				Config.smokeOfDeceit,
//				Config.townPortal,
//				Config.dustOfAppearance,
//				Config.animalCourier,
//				Config.flyingCourier,
//				Config.observerWard,
//				Config.sentryWard,
//				Config.bottle
		);
		populateColumn(
				ItemColumn.COLUMN_ATTRIBUTES,
				Config.ironBranch,
				Config.gauntletsOfStrength,
				Config.slippersOfAgility,
				Config.mantleOfIntelligence,
				Config.circlet,
				Config.beltOfStrength,
				Config.bandOfElvenskin,
				Config.robeOfTheMagi,
				Config.ogreClub,
				Config.bladeOfAlacrity,
				Config.staffOfWizardry,
				Config.ultimateOrb
		);
		populateColumn(
				ItemColumn.COLUMN_ARMAMENTS,
				Config.ringOfProtection,
				Config.quellingBlade,
				Config.stoutShield,
				Config.bladesOfAttack,
				Config.chainmail,
				Config.helmOfIronWill,
				Config.broadsword,
				Config.quarterstaff,
				Config.claymore,
//				Config.javelin,
				Config.platemail,
				Config.mithrilHammer
		);
		
		populateColumn(
				ItemColumn.COLUMN_ARCANE,
//				Config.magicStick,
				Config.sagesMask,
				Config.ringOfRegen,
				Config.bootsOfSpeed,
				Config.glovesOfHaste,
				Config.cloak,
//				Config.gemOfTrueSight,
//				Config.morbidMask,
//				Config.ghostScepter,
				Config.talismanOfEvasion,
				Config.blinkDagger
//				Config.shadowAmulet
		);
		populateColumn(
				ItemColumn.COLUMN_COMMON,
				Config.wraithBand,
				Config.nullTalisman,
//				Config.magicWand,
				Config.bracer
//				Config.poorMansShield,
//				Config.soulRing,
//				Config.phaseBoots,
//				Config.powerTreads,
//				Config.oblivionStaff,
//				Config.perserverance,
//				Config.handOfMidas,
//				Config.bootsOfTravel
		);
//		populateColumn(
//				ItemColumn.COLUMN_SUPPORT,
//				Config.ringOfBasilius,
//				Config.headdress,
//				Config.buckler,
//				Config.urnOfShadows,
//				Config.ringOfAquila,
//				Config.tranquilBoots,
//				Config.medallionOfCourage,
//				Config.arcaneBoots,
//				Config.drumOfEndurance,
//				Config.vladmirsOffering,
//				Config.mekansm,
//				Config.pipeOfInsight
//		);
		populateColumn(
				ItemColumn.COLUMN_CASTER,
				//Config.forceStaff,
//				Config.necronomicon,
				Config.eulsScepter
//				Config.dagon,
//				Config.veilOfDiscord,
//				Config.rodOfAtos,
//				Config.AghanimsScepter,
//				Config.orchidMalevolence,
//				Config.refresherOrb,
//				Config.scytheOfVyse
		);
//		populateColumn(
//				ItemColumn.COLUMN_WEAPONS,
//				Config.crystalys,
//				Config.armletOfMordiggian,
//				Config.skullBasher,
//				Config.shadowBlade,
//				Config.battleFury,
//				Config.etherealBlade,
//				Config.radiance,
//				Config.monkeyKingBar,
//				Config.daedalus,
//				Config.butterfly,
//				Config.divineRapier,
//				Config.abyssalBlade
//		);
//		populateColumn(
//				ItemColumn.COLUMN_ARMOR,
//				Config.hoodOfDefiance,
//				Config.bladeMail,
//				Config.vanguard,
//				Config.soulBooster,
//				Config.blackKingBar,
//				Config.shivasGuard,
//				Config.mantaStyle,
//				Config.bloodstone,
//				Config.linkensSphere,
//				Config.assaultCuirass,
//				Config.heartOfTarrasque
//		);
//		populateColumn(
//				ItemColumn.COLUMN_ARTIFACTS,
//				Config.helmOfTheDominator,
//				Config.maskOfMadness,
//				Config.sange,
//				Config.yasha,
//				Config.maelstrom,
//				Config.diffusalBlade,
//				Config.desolator,
//				Config.heavensHalberd,
//				Config.sangeAndYasha,
//				Config.mjollnir,
//				Config.eyeOfSkadi,
//				Config.satanic
//		);
		populateColumn(
				ItemColumn.COLUMN_SECRET_SHOP,
//				Config.orbOfVenom,
//				Config.ringOfHealth,
				Config.voidStone
//				Config.energyBooster,
//				Config.vitalityBooster,
//				Config.pointBooster,
//				Config.hyperStone,
//				Config.demonEdge,
//				Config.mysticStaff,
//				Config.reaver,
//				Config.eaglesong,
//				Config.sacredRelic
		);
	}
	private static void populateColumn(ItemColumn column, CfgInfo... items) {
		for (int i = 0; i < items.length; i++) {
			Dota2Item item = (Dota2Item)items[i].instance;
			item.shopColumn = column;
			ItemStack stack = new ItemStack(item, item.defaultQuantity);
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null) {
				tag = new NBTTagCompound();
				stack.setTagCompound(tag);
			}
			tag.setBoolean(TAG_IS_SAMPLE, true);
			itemSamples[column.id][i] = stack;
			samplesMap.put(stack.getItem().getClass(), stack);
		}
	}
	public static ItemStack sampleFor(Class<?> clazz) {
		return samplesMap.get(clazz);
	}
	
	public static InventoryShop newFullShop(int rows) {
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
			ItemColumn.COLUMN_ARTIFACTS.id,
			ItemColumn.COLUMN_SECRET_SHOP.id
		}, rows);
	}
	
	private int[] columns;
	private int height;
	private int filteredHeight;
	private ItemStack[][] displayStacks;
	private int scrollRow;
	private String filterStr;
	public InventoryShop(int[] columns, int height) {
		this.columns = columns;
		this.height = height;
		this.scrollRow = 0;
		this.filterStr = "";
		displayStacks = new ItemStack[getColumns()][getRows()];
		updateDisplayStacks();
	}
	public void setFilterStr(String value) {
		if (!this.filterStr.equals(value.toLowerCase())) {
			this.filterStr = value.toLowerCase();
			updateDisplayStacks();
		}
	}
	public void scrollToRow(int row) {
		if (this.scrollRow != row) {
			this.scrollRow = row;
			updateDisplayStacks();
		}
	}
	public int getScrollRow() {
		return scrollRow;
	}
	public int getRows() {
		return height;
	}
	public int getColumns() {
		return columns.length;
	}
	public int getFilteredRows() {
		return filteredHeight;
	}
	
	public boolean contains(Dota2Item item) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == item.shopColumn.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return columns.length*height;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		int xPos = i % columns.length;
		int yPos = (i - xPos)/columns.length;
		return displayStacks[xPos][yPos];
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
	public void setInventorySlotContents(int slotID, ItemStack itemstack) {}

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
	
	private void updateDisplayStacks() {
		int maxFilteredHeight = 0;
		for (int i = 0; i < getColumns(); i++) {
			int columnIndex = columns[i];
			ItemStack[] column = itemSamples[columnIndex];
			List<ItemStack> filteredColumn = new ArrayList<ItemStack>();
			for (int j = 0; j < ROWS; j++) {
				ItemStack stack = column[j];
				if (stack != null && stack.getDisplayName().toLowerCase().contains(filterStr)) {
					filteredColumn.add(stack);
				}
			}
			maxFilteredHeight = Math.max(maxFilteredHeight, filteredColumn.size());
			ItemStack[] displayColumn = new ItemStack[getRows()];
			for (int j = scrollRow; j < filteredColumn.size() && j-scrollRow < getRows(); j++) {
				displayColumn[j-scrollRow] = filteredColumn.get(j);
			}
			displayStacks[i] = displayColumn;
		}
		filteredHeight = maxFilteredHeight;
	}

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
