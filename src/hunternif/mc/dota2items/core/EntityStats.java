package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;

public class EntityStats {
	private static final float minecraftMovementSpeed = 0.1f;
	public static float maxMovementSpeed = 522f;
	
	public int baseHealth = 150;
	public float baseHealthRegen = 0.25f;
	public int baseMana = 0;
	public float baseManaRegen = 0.01f;
	public int baseMovementSpeed = 300;
	public float baseAttackTime = 1.7f;
	public int baseArmor = 0;
	public int baseDamage = 0;
	
	public int baseIntelligence = 20;
	public int baseStrength = 20;
	public int baseAgility = 20;
	
	// Transient stats:
	private int curMana;
	private int curGold;
	
	private List<BuffInstance> appliedBuffs = new ArrayList<BuffInstance>();
	
	public List<BuffInstance> getAppliedBuffs() {
		return new ArrayList<BuffInstance>(appliedBuffs);
	}
	public void addBuff(BuffInstance buffInst) {
		appliedBuffs.add(buffInst);
	}
	public void removeBuff(BuffInstance buffInst) {
		appliedBuffs.remove(buffInst);
	}
	
	public int getMaxHealth() {
		int health = baseHealth;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			health += buffInst.buff.health;
		}
		return health + 19*getStrength();
	}
	
	public int getMaxMana() {
		int mana = baseMana;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			mana += buffInst.buff.mana;
		}
		return mana + 13*getIntelligence();
	}
	
	public int getMana() {
		return curMana;
	}
	public void addOrDrainMana(int value) {
		int newMana = curMana + value;
		int maxMana = getMaxMana();
		if (newMana < 0) newMana = 0;
		if (newMana > maxMana) newMana = maxMana;
		curMana = newMana;
	}
	
	public float getHealthRegen() {
		float regen = baseHealthRegen;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen += buffInst.buff.healthRegen;
		}
		return regen + 0.03f*(float)getStrength();
	}
	
	public float getManaRegen() {
		float regen = baseManaRegen;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen += buffInst.buff.manaRegen;
		}
		return regen + 0.04f*(float)getIntelligence();
	}
	
	/** A percentage. */
	public int getIncreasedAttackSpeed() {
		int aspd = 0;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			aspd += buffInst.buff.attackSpeed;
		}
		aspd += getAgility();
		if (aspd < -80) aspd = -80;
		if (aspd > 400) aspd = 400;
		return aspd;
	}
	
	public float getAttackTime() {
		float aspd = ((float)getIncreasedAttackSpeed())/100f;
		return baseAttackTime / (1f + aspd);
	}
	
	public int getDamage(int weaponDamage, boolean melee) {
		int damage = weaponDamage + this.baseDamage;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			damage += buffInst.buff.damage;
		}
		for (BuffInstance buffInst : getAppliedBuffs()) {
			damage += MathHelper.floor_float((float)damage * ((float)(melee ? buffInst.buff.damagePercentMelee : buffInst.buff.damagePercentRanged)) / 100f);
		}
		System.out.println("Buffed damage from " + weaponDamage + " to " + damage);
		return damage;
	}
	
	public int getArmor(int basicArmor) {
		int armor = basicArmor + this.baseArmor;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			armor += buffInst.buff.armor;
		}
		armor += ((float)getAgility()/7f);
		System.out.println("Buffed armor from " + basicArmor + " to " + armor);
		return armor;
	}
	
	/** @return movement speed calculated for Minecraft. */
	public float getMovementSpeed() {
		float movementSpeed = baseMovementSpeed;
		List<Buff> appliedMSBuffs = new ArrayList<Buff>();
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (!appliedMSBuffs.contains(buffInst.buff)) {
				movementSpeed += buffInst.buff.movementSpeed;
				// Movement speed from the same type of Buff doesn't stack
				appliedMSBuffs.add(buffInst.buff);
			}
		}
		if (movementSpeed > maxMovementSpeed) {
			movementSpeed = maxMovementSpeed;
		}
		return movementSpeed * 0.1f / (float) baseMovementSpeed;
	}
	
	public boolean canAttack() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.disableAttack) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canMove() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.disableMove) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canUseItems() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.disableItems) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isMagicImmune() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.magicImmune) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInvulnerable() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.invulnerable) {
				return true;
			}
		}
		return false;
	}
	
	public int getGold() {
		return curGold;
	}
	public void addOrRemoveGold(int value) {
		int newGold = curGold + value;
		if (newGold < 0) newGold = 0;
		curGold = newGold;
	}
	
	public int getStrength() {
		int str = baseStrength;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			str += buffInst.buff.strength;
		}
		return str;
	}
	public int getAgility() {
		int agi = baseAgility;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			agi += buffInst.buff.agility;
		}
		return agi;
	}
	public int getIntelligence() {
		int intel = baseIntelligence;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			intel += buffInst.buff.intelligence;
		}
		return intel;
	}
}
