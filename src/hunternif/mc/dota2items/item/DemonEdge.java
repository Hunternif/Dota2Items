package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class DemonEdge extends Dota2Item {

	public DemonEdge(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(46);
		setPrice(2400);
		weaponDamage = 7;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}

}
