package hunternif.mc.dota2items.config;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.inventory.Column;
import hunternif.mc.dota2items.item.Dota2Item;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CfgInfo<T> {
	public T instance;
	protected int id;
	public String name;
	protected int price = 0;
	protected List<CfgInfo<? extends Dota2Item>> recipe;
	protected Buff passiveBuff;
	protected float weaponDamage = 0;
	protected Column column;
	protected String description;
	protected boolean dropsOnDeath = false;
	/** Is set to true when weaponDamage is set to > 0 */
	protected boolean isFull3D = false;
	protected CfgInfo<? extends Dota2Item> baseShopItem;
	protected int upgradeLevel = 0;
	
	protected Class type;
	
	protected CfgInfo(int defaultID, String englishName) {
		this.id = defaultID;
		this.name = englishName;
	}
	
	protected void initialize(Field field) {
		this.type = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
	}
	
	public int getID() {
		return isBlock() ? ((Block)instance).blockID : ((Item)instance).itemID;
	}
	public boolean isBlock() {
		return Block.class.isAssignableFrom(type);
	}
	public CfgInfo setPrice(int value) {
		this.price = value;
		return this;
	}
	public CfgInfo setRecipe(CfgInfo<? extends Dota2Item> ... items) {
		this.recipe = Arrays.asList(items);
		return this;
	}
	public CfgInfo setPassiveBuff(Buff buff) {
		this.passiveBuff = buff;
		return this;
	}
	public CfgInfo setWeaponDamage(float value) {
		this.weaponDamage = value;
		if (value > 0) {
			setIsFull3D(true);
		}
		return this;
	}
	public CfgInfo setColumn(Column column) {
		this.column = column;
		return this;
	}
	public CfgInfo setDescription(String description) {
		this.description = description;
		return this;
	}
	public CfgInfo setDropsOnDeath(boolean value) {
		this.dropsOnDeath = value;
		return this;
	}
	public CfgInfo setIsFull3D(boolean value) {
		this.isFull3D = value;
		return this;
	}
	public CfgInfo setBaseShopItem(CfgInfo<? extends Dota2Item> itemInfo) {
		this.baseShopItem = itemInfo;
		return this;
	}
	public CfgInfo setUpgradeLevel(int level) {
		this.upgradeLevel = level;
		return this;
	}
}
