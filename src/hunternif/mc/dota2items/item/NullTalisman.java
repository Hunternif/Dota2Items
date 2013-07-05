package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

@Recipe(ingredients={Circlet.class, MantleOfIntelligence.class})
public class NullTalisman extends Dota2Item {

	public NullTalisman(int id) {
		super(id);
		passiveBuff = new Buff(this).setStrength(3).setAgility(3).setIntelligence(6).setDamage(3);
		setPrice(135);
	}

}
