package hunternif.mc.dota2items.core.buff;

import hunternif.mc.dota2items.Config;
import hunternif.mc.dota2items.item.Dota2Item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;


public class Buff {
	public final String name;
	public final int id;
	
	public boolean disableAttack;
	public boolean disableMove;
	public boolean disableItems;
	
	public boolean magicImmune;
	public boolean invulnerable;
	
	public int health = 0;
	public float healthRegen = 0;
	public int mana = 0;
	public float manaRegen = 0;
	public int manaRegenPercent = 0;
	public int movementSpeed = 0;
	public int attackSpeed = 0;
	public int armor = 0;
	public int damage = 0;
	public int damagePercentMelee = 0;
	public int damagePercentRanged = 0;
	
	public int intelligence = 0;
	public int strength = 0;
	public int agility = 0;
	
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
		this(lastID + 1, Config.forClass(item.getClass()).name);
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
	
	public Buff setDamagePercentMelee(int value) {
		damagePercentMelee = value;
		return this;
	}
	
	public Buff setDamagePercentRanged(int value) {
		damagePercentRanged = value;
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
	
	@Override
	public String toString() {
		return name;
	}
	
	public List<String> linesForDisplay() {
		List<String> list = new ArrayList<String>();
		if (movementSpeed > 0) {
			list.add("+" + EnumChatFormatting.GOLD + movementSpeed + EnumChatFormatting.GRAY + " Movement Speed.");
		}
		if (armor > 0) {
			list.add("+" + EnumChatFormatting.GOLD + armor + EnumChatFormatting.GRAY + " Armor.");
		}
		if (intelligence > 0) {
			list.add("+" + EnumChatFormatting.GOLD + intelligence + EnumChatFormatting.GRAY + " Intelligence.");
		}
		if (manaRegenPercent > 0) {
			list.add("+" + EnumChatFormatting.GOLD + manaRegenPercent + "%" + EnumChatFormatting.GRAY + " Mana Regeneration.");
		}
		//TODO finish this and use annotations.
		return list;
	}
}
