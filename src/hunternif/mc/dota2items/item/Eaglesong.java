package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import hunternif.mc.dota2items.core.buff.Buff;

public class Eaglesong extends Dota2Item {

	public Eaglesong(int id) {
		super(id);
		passiveBuff = new Buff(this).setAgility(25);
		setPrice(3300);
	}

}
