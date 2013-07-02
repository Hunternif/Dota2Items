package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class Chainmail extends Dota2Item {

	public Chainmail(int id) {
		super(id);
		passiveBuff = new Buff(this).setArmor(5);
		setPrice(550);
	}

}
