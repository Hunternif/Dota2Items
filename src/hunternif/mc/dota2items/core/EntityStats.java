package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.entity.IInvulnerableEntity;
import hunternif.mc.dota2items.entity.IMagicImmuneEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityStats implements IExtendedEntityProperties {
	public static final float MINECRAFT_PLAYER_MOVE_SPEED = 0.1f;
	public static final int MAX_MOVE_SPEED = 522;
	public static final int MAX_HP_PER_STR = 19;
	public static final float HP_REGEN_PER_STR = 0.03f;
	public static final int MAX_MANA_PER_INT = 13;
	public static final float MANA_REGEN_PER_INT = 0.04f;
	public static final float ARMOR_PER_AGI = 1f/7f;
	public static final int MIN_ASPD = -80;
	public static final int MAX_ASPD = 400;
	
	public static final int BASE_PLAYER_HP = 150;
	public static final int BASE_PLAYER_STR = 0;
	public static final int BASE_PLAYER_AGI = 0;
	public static final int BASE_PLAYER_INT = 0;
	public static final int BASE_PLAYER_TOTAL_HP = BASE_PLAYER_HP + BASE_PLAYER_STR*MAX_HP_PER_STR;
	
	private static final String TAG_MANA = "D2IMana";
	private static final String TAG_PARTIAL_HP = "D2IPartialHp";
	private static final String TAG_GOLD = "D2IGgold";
	private static final String TAG_BUFFS = "D2IBuffs";
	
	
	public int entityId;
	/** Measured in ticks the Entity has existed. */
	public long lastSyncTime;
	
	public int baseHealth = BASE_PLAYER_HP;
	public float baseHealthRegen = 0.25f;
	public int baseMana = 0;
	public float baseManaRegen = 0.01f;
	public int baseMovementSpeed = 300;
	public float baseAttackTime = 1.7f;
	public int baseArmor = 0;
	public int baseDamage = 0;
	public int baseSpellResistance = 0;
	
	public boolean baseInvulnerable = false;
	public boolean baseMagicImmune = false;
	
	public int baseStrength = BASE_PLAYER_STR;
	public int baseAgility = BASE_PLAYER_AGI;
	public int baseIntelligence = BASE_PLAYER_INT;
	
	// Transient stats:
	// Health is deferred to Minecraft.
	private float curMana = 0;
	private float curGold = 0;
	/** Used to restrict attack interval by AttackTime. */
	public long lastAttackTime = 0;
	
	/** When applying damage or heal directly to the entity, the amount of health
	 * will be truncated due to entity's health being a fixed small integer
	 * (20 half-hearts for the player). In order to apply the damage and heal
	 * amount properly according to Dota mechanics, this partial health will be
	 * "carried over" and applied, when sufficient, at the next heal/damage
	 * instance. */
	public float partialHalfHeart = 0;
	
	private List<BuffInstance> appliedBuffs = new ArrayList<BuffInstance>();
	
	
	public EntityStats(EntityLivingBase entity) {
		entityId = entity.entityId;
		AttributeInstance attrMaxHealth = entity.func_110148_a(SharedMonsterAttributes.field_111267_a);
		// When EntityPlayer is Constructing, all his attributes are null
		float maxHealth = attrMaxHealth == null ? 20f : (float)attrMaxHealth.func_111126_e();
		baseHealth = MathHelper.floor_float(maxHealth * (float)BASE_PLAYER_HP / 20f);
		if (entity instanceof EntityPlayer) {
			baseHealthRegen = 0.25f;
			baseAttackTime = 1.7f;
			baseMovementSpeed = 300;
			//baseSpellResistance = 25; Cancelled so that normal Minecraft magic hurts as much as before.
		} else {
			baseHealthRegen = 0.5f;
			baseAttackTime = 1.0f;
			baseMovementSpeed = 325;
		}
		if (entity instanceof IMagicImmuneEntity) {
			baseMagicImmune = true;
		}
		if (entity instanceof IInvulnerableEntity) {
			baseInvulnerable = true;
		}
	}
	
	public List<BuffInstance> getAppliedBuffs() {
		return new ArrayList<BuffInstance>(appliedBuffs);
	}
	public void addBuff(BuffInstance buffInst) {
		if (!buffInst.buff.stacks) {
			for (BuffInstance curBuffInst : getAppliedBuffs()) {
				if (curBuffInst.buff.id == buffInst.buff.id) {
					return;
				}
			}
		}
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
	public int getHealth(EntityLivingBase entity) {
		float mcHPProportion = (float)entity.func_110143_aJ() / (float)entity.func_110138_aP();
		float halfHeartEquivalent = (float)getMaxHealth() / (float)entity.func_110138_aP();
		return MathHelper.floor_float((float)getMaxHealth()*mcHPProportion + halfHeartEquivalent*partialHalfHeart);
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
		return MathHelper.floor_float(curMana); //Math.min(getMaxMana(), MathHelper.floor_float(curMana));
	}
	/** Only for "technical" use like writing to NBT and syncing over the network. */
	public float getFloatMana() {
		return curMana;
	}
	public void setMana(float amount) {
		curMana = amount;
		clampMana();
	}
	public void addMana(float amount) {
		setMana(curMana + amount);
	}
	public void removeMana(float amount) {
		setMana(curMana - amount);
	}
	
	public float getManaRegen() {
		float regen = baseManaRegen;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen += buffInst.buff.manaRegen;
		}
		regen += MANA_REGEN_PER_INT*(float)getIntelligence();
		for (BuffInstance buffInst : getAppliedBuffs()) {
			regen *= 1f + buffInst.buff.manaRegenPercent/100f;
		}
		return regen;
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
		return (float)getDotaMovementSpeed() * MINECRAFT_PLAYER_MOVE_SPEED / (float) baseMovementSpeed;
	}
	public int getDotaMovementSpeed() {
		int movementSpeed = baseMovementSpeed;
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
		return movementSpeed;
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
		if (baseMagicImmune) {
			return true;
		}
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.magicImmune) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInvulnerable() {
		if (baseInvulnerable) {
			return true;
		}
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.invulnerable) {
				return true;
			}
		}
		return false;
	}
	
	//NOTE I may need to implement the Reliable and Unreliable gold someday.
	public int getGold() {
		return MathHelper.floor_float(curGold);
	}
	public float getFloatGold() {
		return curGold;
	}
	public void setGold(float amount) {
		float newGold = amount;
		if (newGold < 0) newGold = 0;
		curGold = newGold;
	}
	public void addGold(float amount) {
		setGold(curGold + amount);
	}
	public void removeGold(float amount) {
		setGold(curGold - amount);
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
	
	public boolean canEvade() {
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.evasionPercent > 0 && Math.random()*100 <= buffInst.buff.evasionPercent) {
				return true;
			}
		}
		return false;
	}
	
	public float getCriticalMultiplier() {
		List<BuffInstance> critBuffs = new ArrayList<BuffInstance>();
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.critChancePercent > 0) {
				critBuffs.add(buffInst);
			}
		}
		Collections.sort(critBuffs, critDamageComparator);
		for (BuffInstance buffInst : critBuffs) {
			if (Math.random()*100 <= buffInst.buff.critChancePercent) {
				return buffInst.buff.critDamagePercent * 0.01f;
			}
		}
		return 1f;
	}
	/** Provides sorting by Critical Damage in descending order. */
	private static class CritDamageComparator implements Comparator<BuffInstance> {
		@Override
		public int compare(BuffInstance o1, BuffInstance o2) {
			return o1.buff.critDamagePercent > o2.buff.critDamagePercent ? -1 :
				o1.buff.critDamagePercent < o2.buff.critDamagePercent ? 1 : 0;
		}
	}
	private static CritDamageComparator critDamageComparator = new CritDamageComparator();
	
	public float getSpellResistance() {
		float resistance = 0;
		List<Buff> appliedItems = new ArrayList<Buff>();
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.spellResistance != 0 && !appliedItems.contains(buffInst.buff)) {
				if (buffInst.isItemPassiveBuff) {
					appliedItems.add(buffInst.buff);
				}
				resistance += buffInst.buff.spellResistance;
			}
		}
		return resistance * 0.01f;
	}
	
	public float getMagicAmplification() {
		float result = 1f;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.magicAmplify != 0) {
				result *= (float)buffInst.buff.magicAmplify * 0.01f;
			}
		}
		return result;
	}
	
	public int getDamageBlock(boolean melee) {
		int block = 0;
		for (BuffInstance buffInst : getAppliedBuffs()) {
			if (buffInst.buff.damageBlockChance > 0 && Math.random()*100 <= buffInst.buff.damageBlockChance) {
				int curBlock = melee ? buffInst.buff.damageBlockMelee : buffInst.buff.damageBlockRanged;
				if (curBlock > block) block = curBlock;
			}
		}
		return block;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setFloat(TAG_MANA, getFloatMana());
		compound.setFloat(TAG_PARTIAL_HP, partialHalfHeart);
		compound.setInteger(TAG_GOLD, getGold());
		NBTTagList buffsList = new NBTTagList();
		for (BuffInstance buffInst : getAppliedBuffs()) {
			buffsList.appendTag(buffInst.toNBT());
		}
		compound.setTag(TAG_BUFFS, buffsList);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.curMana = compound.getFloat(TAG_MANA);
		this.partialHalfHeart = compound.getFloat(TAG_PARTIAL_HP);
		this.curGold = compound.getInteger(TAG_GOLD);
		NBTTagList buffsList = compound.getTagList(TAG_BUFFS);
		for (int i = 0; i < buffsList.tagCount(); i++) {
			NBTTagCompound buffTag = (NBTTagCompound) buffsList.tagAt(i);
			BuffInstance buffInst = BuffInstance.fromNBT(buffTag, entityId);
			addBuff(buffInst);
		}
	}

	@Override
	public void init(Entity entity, World world) {
	}
	
	public void clampMana() {
		int maxMana = getMaxMana();
		if (curMana < 0) curMana = 0;
		if (curMana > maxMana) curMana = maxMana;
	}
}
