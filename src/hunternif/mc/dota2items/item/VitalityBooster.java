package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class VitalityBooster extends Dota2Item {

	public VitalityBooster(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealth(250);
		setPrice(1100);
	}

}
