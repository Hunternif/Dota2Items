package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class SlippersOfAgility extends Dota2Item {
	
	public SlippersOfAgility(int id) {
		super(id);
		passiveBuff = new Buff(this).setAgility(3);
		setPrice(150);
	}
	
}
