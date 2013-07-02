package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class HelmOfIronWill extends Dota2Item {

	public HelmOfIronWill(int id) {
		super(id);
		passiveBuff = new Buff(this).setArmor(5).setHealthRegen(3);
		setPrice(950);
	}

}
