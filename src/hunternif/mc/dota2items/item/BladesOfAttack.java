package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class BladesOfAttack extends Dota2Item {

	public BladesOfAttack(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setDamage(9));
		setPrice(450);
		weaponDamage = 5;
	}

}
