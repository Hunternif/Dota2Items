package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

@Recipe(ingredients={Circlet.class, SlippersOfAgility.class})
public class WraithBand extends Dota2Item {

	public WraithBand(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(3).setAgility(6).setIntelligence(3).setDamage(3));
		setPrice(150);
	}

}
