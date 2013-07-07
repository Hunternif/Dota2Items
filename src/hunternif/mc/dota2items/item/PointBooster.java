package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class PointBooster extends Dota2Item {

	public PointBooster(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealth(200).setMana(150);
		setPrice(1200);
	}

}
