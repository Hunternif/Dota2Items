package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class SacredRelic extends Dota2Item {

	public SacredRelic(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(60);
		setPrice(3800);
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
