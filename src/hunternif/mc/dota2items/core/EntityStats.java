package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

public class EntityStats {
	private static final float MINECRAFT_MOVE_SPEED = 0.1f;
	public static final int MAX_MOVE_SPEED = 522;
	public static final int MAX_HP_PER_STR = 19;
	public static final float HP_REGEN_PER_STR = 0.03f;
	public static final int MAX_MANA_PER_INT = 13;
	public static final float MANA_REGEN_PER_INT = 0.04f;
	public static final float ARMOR_PER_AGI = 1f/7f;
	public static final int MIN_ASPD = -80;
	public static final int MAX_ASPD = 400;
	
	public static final int BASE_HP = 150;
	public static final int BASE_STR = 0;
	public static final int BASE_AGI = 0;
	public static final int BASE_INT = 0;
	public static final int BASE_TOTAL_HP = BASE_HP + BASE_STR*MAX_HP_PER_STR;
	
	
	public int baseHealth = BASE_HP;
	public float baseHealthRegen = 0.25f;
	public int baseMana = 0;
	public float baseManaRegen = 0.01f;
	public int baseMovementSpeed = 300;
	public float baseAttackTime = 1.7f;
	public int baseArmor = 0;
	public int baseDamage = 0;
	
	public int baseStrength = BASE_STR;
	public int baseAgility = BASE_AGI;
	public int baseIntelligence = BASE_INT;
	
	// Transient stats:
	//private int curHealth; // Health is deferred to Minecraft.
	private float curMana = 0;
	private float curGold = 0;
	/** Used to restrict attack interval by AttackTime. */
	public long lastAttackTime = 0;
	/** When health is restored by Dota 2 regen, it can only apply to Steve's
	 * actual 20 HP. Therefore, until health is restored enough to fill 1/2 heart
	 * it is stored in this variable. */
	public float carryOverDotaHealthRestored = 0;
	/** Until damage is dealt enough to deplete 1/2 heart it is stored in this
	 * variable. This is Minecraft damage, not Dota!*/
	public float carryOverMinecraftDamage = 0;
	
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
		return health + MAX_HP_PER_STR*getStrength();
	}
	public int getHealth(EntityLiving entity) {
		float rate = (float)entity.getHealth() / (float)entity.getMaxHealth();
		return MathHelper.floor_float((float)getMaxHealth()*rate + carryOverDotaHealthRestored);
	}
	public float getHealthRegen() {
		float regen = baseHealthRegen;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen += buffInst.buff.healthRegen;
		}
		return regen + HP_REGEN_PER_STR*(float)getStrength();
	}
	
	public int getMaxMana() {
		int mana = baseMana;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			mana += buffInst.buff.mana;
		}
		return mana + MAX_MANA_PER_INT*getIntelligence();
	}
	public int getMana() {
		return MathHelper.floor_float(curMana);
	}
	/** You can add fractional amount of mana, but you can retrieve only integer. */
	public void addOrDrainMana(float value) {
		float newMana = curMana + value;
		float maxMana = getMaxMana();
		if (newMana < 0) newMana = 0;
		if (newMana > maxMana) newMana = maxMana;
		curMana = newMana;
	}
	
	public float getManaRegen() {
		float regen = baseManaRegen;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen += buffInst.buff.manaRegen;
		}
		return regen + MANA_REGEN_PER_INT*(float)getIntelligence();
	}
	
	/** A percentage. */
	public int getIncreasedAttackSpeed() {
		int aspd = 0;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			aspd += buffInst.buff.attackSpeed;
		}
		aspd += getAgility();
		if (aspd < MIN_ASPD) aspd = MIN_ASPD;
		if (aspd > MAX_ASPD) aspd = MAX_ASPD;
		return aspd;
	}
	
	public float getAttackTime() {
		float aspd = ((float)getIncreasedAttackSpeed())/100f;
		float attackTime = baseAttackTime / (1f + aspd);
		//FMLLog.log(Dota2Items.ID, Level.FINE, "Attack time = %.2f", attackTime);
		return attackTime;
	}
	
	public float getDamage(float weaponDamage, boolean melee) {
		float damage = weaponDamage + this.baseDamage;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			damage += buffInst.buff.damage;
		}
		for (BuffInstance buffInst : getAppliedBuffs()) {
			damage += (float)damage * ((float)(melee ? buffInst.buff.damagePercentMelee : buffInst.buff.damagePercentRanged)) / 100f;
		}
		//FMLLog.log(Dota2Items.ID, Level.FINE, "Buffed damage from %.2f to %.2f", weaponDamage, damage);
		return damage;
	}
	
	public int getArmor(int basicArmor) {
		int armor = basicArmor + this.baseArmor;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			armor += buffInst.buff.armor;
		}
		armor += (float)getAgility() * ARMOR_PER_AGI;
		//FMLLog.log(Dota2Items.ID, Level.FINE, "Buffed armor from %d to %d", basicArmor, armor);
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
		if (movementSpeed > MAX_MOVE_SPEED) {
			movementSpeed = MAX_MOVE_SPEED;
		}
		return movementSpeed * MINECRAFT_MOVE_SPEED / (float) baseMovementSpeed;
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
		return MathHelper.floor_float(curGold);
	}
	/** You can add fractional amount of gold, but you can retrieve only integer. */
	public void addOrRemoveGold(float amount) {
		float newGold = curGold + amount;
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
