package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class Broadsword extends Dota2Item {

	public Broadsword(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(18);
		setPrice(1200);
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
