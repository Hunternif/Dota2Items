package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class Reaver extends Dota2Item {

	public Reaver(int id) {
		super(id);
		passiveBuff = new Buff(this).setStrength(25);
		setPrice(3200);
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 7;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
