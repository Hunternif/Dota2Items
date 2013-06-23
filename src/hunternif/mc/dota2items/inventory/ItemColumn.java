package hunternif.mc.dota2items.inventory;

public enum ItemColumn {
	COLUMN_CONSUMABLES(0, "Consumables"),
	COLUMN_ATTRIBUTES(1, "Attributes"),
	COLUMN_ARMAMENTS(2, "Armaments"),
	COLUMN_ARCANE(3, "Arcane"),
	COLUMN_COMMON(4, "Common"),
	COLUMN_SUPPORT(5, "Support"),
	COLUMN_CASTER(6, "Caster"),
	COLUMN_WEAPONS(7, "Weapons"),
	COLUMN_ARMOR(8, "Armor"),
	COLUMN_ARTIFACTS(9, "Artifacts"),
	COLUMN_SECRET_SHOP(10, "Secret shop");
	
	public final int id;
	public final String name;
	
	ItemColumn(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static ItemColumn forId(int id) {
		for (ItemColumn column: values()) {
			if (column.id == id) {
				return column;
			}
		}
		return null;
	}
}
