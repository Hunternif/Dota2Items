package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class MantleOfIntelligence extends Dota2Item {
	
	public MantleOfIntelligence(int id) {
		super(id);
		passiveBuff = new Buff(this).setIntelligence(3);
		setPrice(150);
	}
	
}
