package hunternif.mc.dota2items.gui;

import hunternif.mc.dota2items.inventory.ItemColumn;
import net.minecraft.util.EnumChatFormatting;

public class ColorHelper {
	public static int colourForColumn(ItemColumn column) {
		if (column == null) {
			return 0xffffff;
		}
		switch (column) {
		case COLUMN_CONSUMABLES:
			return 0x1D80E7;
		case COLUMN_ATTRIBUTES:
		case COLUMN_ARMAMENTS:
		case COLUMN_ARCANE:
			return 0xffffff;
		case COLUMN_COMMON:
			return 0x2BAB01;
		case COLUMN_SUPPORT:
		case COLUMN_CASTER:
			return 0x1A87F9;
		case COLUMN_WEAPONS:
		case COLUMN_ARMOR:
			return 0xB812F9;
		case COLUMN_ARTIFACTS:
			return 0xE29B01;
		case COLUMN_SECRET_SHOP:
			return 0xffffff;
		default:
			return -1;
		}
	}
	
	public static EnumChatFormatting prefixForColumn(ItemColumn column) {
		if (column == null) {
			return EnumChatFormatting.WHITE;
		}
		switch (column) {
		case COLUMN_CONSUMABLES:
			return EnumChatFormatting.BLUE;
		case COLUMN_ATTRIBUTES:
		case COLUMN_ARMAMENTS:
		case COLUMN_ARCANE:
			return EnumChatFormatting.WHITE;
		case COLUMN_COMMON:
			return EnumChatFormatting.DARK_GREEN;
		case COLUMN_SUPPORT:
		case COLUMN_CASTER:
			return EnumChatFormatting.BLUE;
		case COLUMN_WEAPONS:
		case COLUMN_ARMOR:
			return EnumChatFormatting.DARK_PURPLE;
		case COLUMN_ARTIFACTS:
			return EnumChatFormatting.GOLD;
		case COLUMN_SECRET_SHOP:
		default:
			return EnumChatFormatting.WHITE;
		}
	}
}
