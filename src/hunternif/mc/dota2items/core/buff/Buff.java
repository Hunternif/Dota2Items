package hunternif.mc.dota2items.core.buff;

import hunternif.mc.dota2items.config.DescriptionBuilder.BuffLineFormat;
import hunternif.mc.dota2items.item.Dota2Item;

public class Buff {
	public final String name;
	public final int id;
	
	public boolean disableAttack;
	public boolean disableMove;
	public boolean disableItems;
	
	public boolean magicImmune;
	public boolean invulnerable;
	
	@BuffLineFormat("+{%d} Health")
	public int health = 0;
	@BuffLineFormat("+{%.0f} HP Regeneration")
	public float healthRegen = 0;
	@BuffLineFormat("+{%d} Mana")
	public int mana = 0;
	public float manaRegen = 0;
	@BuffLineFormat("+{%d%%} Mana Regeneration")
	public int manaRegenPercent = 0;
	@BuffLineFormat("+{%d} Movement Speed")
	public int movementSpeed = 0;
	@BuffLineFormat("+{%d} Attack Speed")
	public int attackSpeed = 0;
	@BuffLineFormat("+{%d} Armor")
	public int armor = 0;
	@BuffLineFormat("+{%d} Damage")
	public int damage = 0;
	public int damagePercentMelee = 0;
	public int damagePercentRanged = 0;

	@BuffLineFormat("[Melee Block:] {%d}")
	public int damageBlockMelee = 0;
	@BuffLineFormat("[Ranged Block:] {%d}")
	public int damageBlockRanged = 0;
	@BuffLineFormat("[Block Chance:] {%d%%}")
	public int damageBlockChance = 0;
	
	@BuffLineFormat("+{%d} Intelligence")
	public int intelligence = 0;
	@BuffLineFormat("+{%d} Strength")
	public int strength = 0;
	@BuffLineFormat("+{%d} Agility")
	public int agility = 0;
	
	@BuffLineFormat("+{%d%%} Evasion")
	public int evasionPercent = 0;
	public boolean trueStrike = false;
	
	@BuffLineFormat("[Critical Chance:] {%d%%}")
	public int critChancePercent = 0;
	@BuffLineFormat("[Critical Damage:] {%d%%}")
	public int critDamagePercent = 0;
	
	@BuffLineFormat("+{%d%%} Spell resistance")
	public int spellResistance = 0;
	@BuffLineFormat("[Bonus Magic Damage:] {%d%%}")
	public int magicAmplify = 0;
	
	public boolean stacks = true;
	
	
	private static int lastID = 0;
	public static final Buff[] buffList = new Buff[1024];
	
	public static final Buff tangoActive = new Buff(1, "Tango active").setHealthRegen(115f/16f).setDoesNotStack();
	public static final Buff inCyclone = new Buff(2, "Swept up in Cyclone").setDisableAttack().setDisableItems().setDisableMove().setInvulnerable().setMagicImmune();
	
	
	public Buff(int id, String name) {
		this.id = id;
		this.name = name;
		buffList[id] = this;
		lastID = id;
	}
	/**
	 * For passive item buffs. ID is not specified, because it is never sent
	 * for passive buffs.
	 */
	public Buff(Dota2Item item) {
		this(lastID + 1, item.getLocalizedName(null));
	}
	/**
	 * For passive item buffs. ID is not specified, because it is never sent
	 * for passive buffs.
	 */
	public Buff(String name) {
		this(lastID + 1, name);
	}
	
	public Buff setDisableAttack() {
		disableAttack = true;
		return this;
	}
	
	public Buff setDisableMove() {
		disableMove = true;
		return this;
	}
	
	public Buff setDisableItems() {
		disableItems = true;
		return this;
	}
	
	public Buff setMagicImmune() {
		magicImmune = true;
		return this;
	}
	
	public Buff setInvulnerable() {
		invulnerable = true;
		return this;
	}
	
	public Buff setHealth(int value) {
		health = value;
		return this;
	}
	
	public Buff setHealthRegen(float value) {
		healthRegen = value;
		return this;
	}
	
	public Buff setMana(int value) {
		mana = value;
		return this;
	}
	
	public Buff setManaRegen(float value) {
		manaRegen = value;
		return this;
	}
	
	public Buff setManaRegenPercent(int value) {
		manaRegenPercent = value;
		return this;
	}
	
	public Buff setMovementSpeed(int value) {
		movementSpeed = value;
		return this;
	}
	
	public Buff setAttackSpeed(int value) {
		attackSpeed = value;
		return this;
	}
	
	public Buff setArmor(int value) {
		armor = value;
		return this;
	}
	
	public Buff setDamage(int value) {
		damage = value;
		return this;
	}
	public Buff setDamagePercent(int melee, int ranged) {
		damagePercentMelee = melee;
		damagePercentRanged = ranged;
		return this;
	}
	
	public Buff setStrength(int value) {
		strength = value;
		return this;
	}
	
	public Buff setIntelligence(int value) {
		intelligence = value;
		return this;
	}
	
	public Buff setAgility(int value) {
		agility = value;
		return this;
	}
	
	public Buff setDoesNotStack() {
		stacks = false;
		return this;
	}
	
	public Buff setEvasionPercent(int value) {
		evasionPercent = value;
		return this;
	}
	
	public Buff setCrit(int chancePercent, int damagePercent) {
		critChancePercent = chancePercent;
		critDamagePercent = damagePercent;
		return this;
	}
	
	public Buff setSpellResistance(int value) {
		spellResistance = value;
		return this;
	}
	public Buff setMagicAmplify(int value) {
		magicAmplify = value;
		return this;
	}
	
	public Buff setDamageBlock(int melee, int ranged, int chancePercent) {
		damageBlockMelee = melee;
		damageBlockRanged = ranged;
		damageBlockChance = chancePercent;
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
