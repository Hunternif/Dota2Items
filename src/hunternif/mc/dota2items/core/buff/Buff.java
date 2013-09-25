package hunternif.mc.dota2items.core.buff;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.config.DescriptionBuilder.BuffLineFormat;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.util.ResourceLocation;

public class Buff {
	public final String name;
	public final int id;
	
	public boolean disableAttack;
	public boolean disableMove;
	public boolean disableItems;
	
	public boolean magicImmune;
	public boolean invulnerable;
	
	public boolean isRemovedOnHurt;
	public boolean isRemovedOnAction;
	
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
	@BuffLineFormat("+{%d} Movement Speed")
	public int bootsMovementSpeed = 0;
	@BuffLineFormat("+{%d%%} Movement Speed")
	public int movementSpeedPercent = 0;
	@BuffLineFormat("+{%d} Attack Speed")
	public int attackSpeed = 0;
	@BuffLineFormat("+{%d} Armor")
	public int armor = 0;
	@BuffLineFormat("+{%d} Damage")
	public int damage = 0;
	public int damagePercentMelee = 0;
	public int damagePercentRanged = 0;
	
	@BuffLineFormat("[Pierce damage:] {%d}")
	public int bonusDamage = 0;
	@BuffLineFormat("[Chance to Pierce:] {%d%%}")
	public int bonusDamageChance = 0;

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
	
	public int amplifyMagic = 0;
	public int amplifyDamage = 0;
	
	@BuffLineFormat("[Lifesteal:] {%d%%")
	public int lifesteal = 0;
	
	public boolean stacks = true;
	
	public boolean isDisplayed = false;
	public ResourceLocation iconTexture;
	
	
	private static int lastID = 0;
	public static final Buff[] buffList = new Buff[1024];
	
	public static final Buff tango = new Buff(1, "Eat Tree").setHealthRegen(115f/16f).setDoesNotStack().setIsDisplayed().setIconTexture("items/tango.png");
	public static final Buff inCyclone = new Buff(2, "Swept up in Cyclone").setDisableAttack().setDisableItems().setDisableMove().setInvulnerable().setMagicImmune().setIsDisplayed();
	public static final Buff clarity = new Buff(3, "Clarity").setManaRegen(100f/30f).setIsRemovedOnHurt().setDoesNotStack().setIsDisplayed().setIconTexture("items/clarity.png");
	public static final Buff salve = new Buff(4, "Regenerate").setHealthRegen(400f/10f).setIsRemovedOnHurt().setDoesNotStack().setIsDisplayed().setIconTexture("items/healingSalve.png");
	public static final Buff phase = new Buff(5, "Phase").setMovementSpeedPercent(16).setIsRemovedOnAction().setDoesNotStack().setIsDisplayed().setIconTexture("items/phaseBoots.png");
	public static final Buff berserk = new Buff(6, "Berserk").setMovementSpeedPercent(30).setAttackSpeed(100).setAmplifyDamage(30, 0).setIsDisplayed().setIconTexture("items/maskOfMadness.png");
	public static final Buff force = new Buff(7, "Force").setIsDisplayed();
	
	
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
		this(lastID + 1, item.getStatName());
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
	
	public Buff setMovementSpeed(int value, boolean isBoots) {
		if (isBoots) {
			bootsMovementSpeed = value;
		} else {
			movementSpeed = value;
		}
		return this;
	}
	
	public Buff setMovementSpeedPercent(int value) {
		movementSpeedPercent = value;
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
	
	public Buff setBonusDamage(int damage, int chance) {
		bonusDamage = damage;
		bonusDamageChance = chance;
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
	
	public Buff setIsDisplayed() {
		isDisplayed = true;
		return this;
	}
	public Buff setIconTexture(String texture) {
		iconTexture = new ResourceLocation(Dota2Items.ID+":textures/" + texture);
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
	public Buff setAmplifyDamage(int physical, int magical) {
		amplifyDamage = physical;
		amplifyMagic = magical;
		return this;
	}
	
	public Buff setDamageBlock(int melee, int ranged, int chancePercent) {
		damageBlockMelee = melee;
		damageBlockRanged = ranged;
		damageBlockChance = chancePercent;
		return this;
	}
	public int getDamageBlock(boolean melee, boolean isHero) {
		if (damageBlockChance > 0 && Math.random()*100 <= damageBlockChance) {
			return melee ? damageBlockMelee : damageBlockRanged;
		}
		return 0;
	}
	
	public Buff setLifesteal(int percent) {
		this.lifesteal = percent;
		return this;
	}
	
	public Buff setIsRemovedOnHurt() {
		this.isRemovedOnHurt = true;
		return this;
	}
	public Buff setIsRemovedOnAction() {
		this.isRemovedOnAction = true;
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
