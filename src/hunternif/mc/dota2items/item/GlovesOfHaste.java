package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class GlovesOfHaste extends Dota2Item {

	public GlovesOfHaste(int id) {
		super(id);
		passiveBuff = new Buff(this).setAttackSpeed(15);
		setPrice(500);
	}

}
