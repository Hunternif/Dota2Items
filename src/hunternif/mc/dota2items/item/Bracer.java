package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;

@Recipe(ingredients={Circlet.class, GauntletsOfStrength.class})
public class Bracer extends Dota2Item {

	public Bracer(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setStrength(6).setAgility(3).setIntelligence(3).setDamage(3));
		setPrice(190);
	}

}
