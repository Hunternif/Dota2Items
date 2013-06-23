package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class StaffOfWizardry extends Dota2Item {

	public StaffOfWizardry(int id) {
		super(id);
		passiveBuff = new Buff(this).setIntelligence(10);
		setPrice(1000);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
