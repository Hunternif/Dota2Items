package hunternif.mc.dota2items.inventory;

public enum Column {
	CONSUMABLES(0, "Consumables"),
	ATTRIBUTES(1, "Attributes"),
	ARMAMENTS(2, "Armaments"),
	ARCANE(3, "Arcane"),
	COMMON(4, "Common"),
	SUPPORT(5, "Support"),
	CASTER(6, "Caster"),
	WEAPONS(7, "Weapons"),
	ARMOR(8, "Armor"),
	ARTIFACTS(9, "Artifacts"),
	SECRET_SHOP(10, "Secret shop");
	
	public final int id;
	public final String name;
	
	Column(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static Column forId(int id) {
		for (Column column: values()) {
			if (column.id == id) {
				return column;
			}
		}
		return null;
	}
}
