package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class Claymore extends Dota2Item {

	public Claymore(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(21);
		setPrice(1400);
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
