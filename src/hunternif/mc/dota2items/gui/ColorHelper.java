package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.inventory.InventoryShop;
import net.minecraft.util.EnumChatFormatting;

public class ColorHelper {
	public static int colourForColumn(int columnId) {
		switch (columnId) {
		case InventoryShop.COLUMN_CONSUMABLES:
			return 0x1D80E7;
		case InventoryShop.COLUMN_ATTRIBUTES:
		case InventoryShop.COLUMN_ARMAMENTS:
		case InventoryShop.COLUMN_ARCANE:
			return 0xffffff;
		case InventoryShop.COLUMN_COMMON:
			return 0x2BAB01;
		case InventoryShop.COLUMN_SUPPORT:
		case InventoryShop.COLUMN_CASTER:
			return 0x1A87F9;
		case InventoryShop.COLUMN_WEAPONS:
		case InventoryShop.COLUMN_ARMOR:
			return 0xB812F9;
		case InventoryShop.COLUMN_ARTIFACTS:
			return 0xE29B01;
		case InventoryShop.COLUMN_SECRET_SHOP:
			return 0xffffff;
		default:
			return -1;
		}
	}
	
	public static EnumChatFormatting prefixForColumn(int columnId) {
		switch (columnId) {
		case InventoryShop.COLUMN_CONSUMABLES:
			return EnumChatFormatting.BLUE;
		case InventoryShop.COLUMN_ATTRIBUTES:
		case InventoryShop.COLUMN_ARMAMENTS:
		case InventoryShop.COLUMN_ARCANE:
			return EnumChatFormatting.WHITE;
		case InventoryShop.COLUMN_COMMON:
			return EnumChatFormatting.DARK_GREEN;
		case InventoryShop.COLUMN_SUPPORT:
		case InventoryShop.COLUMN_CASTER:
			return EnumChatFormatting.BLUE;
		case InventoryShop.COLUMN_WEAPONS:
		case InventoryShop.COLUMN_ARMOR:
			return EnumChatFormatting.DARK_PURPLE;
		case InventoryShop.COLUMN_ARTIFACTS:
			return EnumChatFormatting.GOLD;
		case InventoryShop.COLUMN_SECRET_SHOP:
		default:
			return EnumChatFormatting.WHITE;
		}
	}
}
