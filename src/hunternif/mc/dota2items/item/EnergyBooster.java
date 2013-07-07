package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class EnergyBooster extends Dota2Item {

	public EnergyBooster(int id) {
		super(id);
		passiveBuff = new Buff(this).setMana(250);
		setPrice(1000);
	}

}
