package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;

public class TalismanOfEvasion extends Dota2Item {

	public TalismanOfEvasion(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setEvasionPercent(25));
		setPrice(1800);
	}

}
