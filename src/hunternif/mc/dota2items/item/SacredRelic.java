package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class SacredRelic extends Dota2Item {

	public SacredRelic(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(60);
		setPrice(3800);
		weaponDamage = 7;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}

}
