package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class RingOfRegen extends Dota2Item {

	public RingOfRegen(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealthRegen(2);
		setPrice(350);
	}

}
