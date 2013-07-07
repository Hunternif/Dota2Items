package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class DemonEdge extends Dota2Item {

	public DemonEdge(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(46);
		setPrice(2400);
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 7;
	}

}
