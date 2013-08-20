package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class OgreClub extends Dota2Item {

	public OgreClub(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(10));
		setPrice(1000);
		weaponDamage = 7;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
