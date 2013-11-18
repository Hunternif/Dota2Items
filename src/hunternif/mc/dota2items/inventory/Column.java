package hunternif.mc.dota2items.inventory;

import net.minecraft.util.EnumChatFormatting;

public enum Column {
	CONSUMABLES(0, "Consumables", 0x1D80E7, EnumChatFormatting.BLUE),
	ATTRIBUTES(1, "Attributes", 0xffffff, EnumChatFormatting.WHITE),
	ARMAMENTS(2, "Armaments", 0xffffff, EnumChatFormatting.WHITE),
	ARCANE(3, "Arcane", 0xffffff, EnumChatFormatting.WHITE),
	COMMON(4, "Common", 0x2BAB01, EnumChatFormatting.DARK_GREEN),
	SUPPORT(5, "Support", 0x1A87F9, EnumChatFormatting.BLUE),
	CASTER(6, "Caster", 0x1A87F9, EnumChatFormatting.BLUE),
	WEAPONS(7, "Weapons", 0xB812F9, EnumChatFormatting.DARK_PURPLE),
	ARMOR(8, "Armor", 0xB812F9, EnumChatFormatting.DARK_PURPLE),
	ARTIFACTS(9, "Artifacts", 0xE29B01, EnumChatFormatting.GOLD),
	SECRET_SHOP(10, "Secret shop", 0xffffff, EnumChatFormatting.WHITE);
	
	public final int id;
	public final String name;
	public final int colorHex;
	public final EnumChatFormatting colorSymbol;
	
	Column(int id, String name, int colorHex, EnumChatFormatting colorSymbol) {
		this.id = id;
		this.name = name;
		this.colorHex = colorHex;
		this.colorSymbol = colorSymbol;
	}
	
	public static Column forId(int id) {
		for (Column column: values()) {
			if (column.id == id) {
				return column;
			}
		}
		return null;
	}
	
	public String getColoredName() {
		return colorSymbol + name;
	}
}
