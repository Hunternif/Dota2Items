package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class BeltOfStrength extends Dota2Item {
	
	public BeltOfStrength(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(6));
		setPrice(450);
	}
	
}
