package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class Platemail extends Dota2Item {

	public Platemail(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setArmor(10));
		setPrice(1400);
	}

}
