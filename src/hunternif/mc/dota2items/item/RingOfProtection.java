package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class RingOfProtection extends Dota2Item {

	public RingOfProtection(int id) {
		super(id);
		passiveBuff = new Buff(this).setArmor(2);
		setPrice(175);
	}

}
