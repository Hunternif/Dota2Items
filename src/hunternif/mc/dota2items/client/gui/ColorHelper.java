package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.inventory.Column;
import net.minecraft.util.EnumChatFormatting;

public class ColorHelper {
	public static int colourForColumn(Column column) {
		if (column == null) {
			return 0xffffff;
		}
		switch (column) {
		case CONSUMABLES:
			return 0x1D80E7;
		case ATTRIBUTES:
		case ARMAMENTS:
		case ARCANE:
			return 0xffffff;
		case COMMON:
			return 0x2BAB01;
		case SUPPORT:
		case CASTER:
			return 0x1A87F9;
		case WEAPONS:
		case ARMOR:
			return 0xB812F9;
		case ARTIFACTS:
			return 0xE29B01;
		case SECRET_SHOP:
			return 0xffffff;
		default:
			return -1;
		}
	}
	
	public static EnumChatFormatting prefixForColumn(Column column) {
		if (column == null) {
			return EnumChatFormatting.WHITE;
		}
		switch (column) {
		case CONSUMABLES:
			return EnumChatFormatting.BLUE;
		case ATTRIBUTES:
		case ARMAMENTS:
		case ARCANE:
			return EnumChatFormatting.WHITE;
		case COMMON:
			return EnumChatFormatting.DARK_GREEN;
		case SUPPORT:
		case CASTER:
			return EnumChatFormatting.BLUE;
		case WEAPONS:
		case ARMOR:
			return EnumChatFormatting.DARK_PURPLE;
		case ARTIFACTS:
			return EnumChatFormatting.GOLD;
		case SECRET_SHOP:
		default:
			return EnumChatFormatting.WHITE;
		}
	}
}
