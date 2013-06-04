package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.mechanics.buff.Buff;
import hunternif.mc.dota2items.mechanics.buff.BuffInstance;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;

public class EntityStats {
	private static final float minecraftMovementSpeed = 0.1f;
	public static float maxMovementSpeed = 522f;
	
	public int baseHealth = 500;
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
	// Perhaps someday I'll make the attributes work.
	
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
	
	public int getHealth() {
		int health = baseHealth;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			health += buffInst.buff.health;
		}
		return health;
	}
	
	public int getMana() {
		int mana = baseMana;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			mana += buffInst.buff.mana;
		}
		return mana;
	}
	
	//TODO code all this same stuff
	
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
}
