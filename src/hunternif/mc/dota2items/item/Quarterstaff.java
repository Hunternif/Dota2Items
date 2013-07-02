package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class Quarterstaff extends Dota2Item {

	public Quarterstaff(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(10).setAttackSpeed(10);
		setPrice(900);
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 4;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
