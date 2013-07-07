package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class RingOfHealth extends Dota2Item {

	public RingOfHealth(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealthRegen(5);
		setPrice(875);
	}

}
