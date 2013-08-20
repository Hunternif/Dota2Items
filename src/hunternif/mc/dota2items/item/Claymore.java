package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class Claymore extends Dota2Item {

	public Claymore(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setDamage(21));
		setPrice(1400);
		weaponDamage = 7;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
