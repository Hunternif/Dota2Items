package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class Hyperstone extends Dota2Item {

	public Hyperstone(int id) {
		super(id);
		passiveBuff = new Buff(this).setAttackSpeed(55);
		setPrice(2100);
	}

}
