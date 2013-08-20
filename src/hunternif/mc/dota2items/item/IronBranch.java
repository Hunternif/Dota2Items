package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class IronBranch extends Dota2Item {
	
	public IronBranch(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(1).setAgility(1).setIntelligence(1));
		setPrice(53);
	}
	
}
