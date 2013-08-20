package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class MysticStaff extends Dota2Item {

	public MysticStaff(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setIntelligence(25));
		setPrice(2700);
		weaponDamage = 2;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
