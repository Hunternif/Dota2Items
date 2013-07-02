package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class MithrilHammer extends Dota2Item {

	public MithrilHammer(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(24);
		setPrice(1600);
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
