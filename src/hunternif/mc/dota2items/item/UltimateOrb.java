package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class UltimateOrb extends Dota2Item {

	public UltimateOrb(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(10).setAgility(10).setIntelligence(10));
		setPrice(2100);
	}
}
