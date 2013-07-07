package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class MysticStaff extends Dota2Item {

	public MysticStaff(int id) {
		super(id);
		passiveBuff = new Buff(this).setIntelligence(25);
		setPrice(2700);
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 2;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
