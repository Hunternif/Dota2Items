package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class RobeOfTheMagi extends Dota2Item {
	
	public RobeOfTheMagi(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setIntelligence(6));
		setPrice(450);
	}
	
}
