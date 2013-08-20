package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class Broadsword extends Dota2Item {

	public Broadsword(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setDamage(18));
		setPrice(1200);
		weaponDamage = 7;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
