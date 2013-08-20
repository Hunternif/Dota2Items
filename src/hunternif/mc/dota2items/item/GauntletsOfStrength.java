package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class GauntletsOfStrength extends Dota2Item {
	
	public GauntletsOfStrength(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(3));
		setPrice(150);
	}
	
}
