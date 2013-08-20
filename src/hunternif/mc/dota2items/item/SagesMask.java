package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class SagesMask extends Dota2Item {

	public SagesMask(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setManaRegenPercent(50));
		setPrice(325);
	}

}
