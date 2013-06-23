package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class VoidStone extends Dota2Item {

	public VoidStone(int id) {
		super(id);
		passiveBuff = new Buff(this).setManaRegenPercent(100);
		setPrice(875);
	}

}
