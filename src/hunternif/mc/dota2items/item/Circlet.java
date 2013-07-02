package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class Circlet extends Dota2Item {

	public Circlet(int id) {
		super(id);
		passiveBuff = new Buff(this).setStrength(2).setAgility(2).setIntelligence(2);
		setPrice(185);
	}
}
