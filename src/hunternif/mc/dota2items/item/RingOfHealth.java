package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class RingOfHealth extends Dota2Item {

	public RingOfHealth(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setHealthRegen(5));
		setPrice(875);
	}

}
