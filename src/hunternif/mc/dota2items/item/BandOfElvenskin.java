package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class BandOfElvenskin extends Dota2Item {
	
	public BandOfElvenskin(int id) {
		super(id);
		passiveBuff = new Buff(this).setAgility(6);
		setPrice(450);
	}
	
}
