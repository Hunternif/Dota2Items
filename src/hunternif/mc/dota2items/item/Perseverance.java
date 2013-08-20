package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;

@Recipe(ingredients={RingOfHealth.class, VoidStone.class})
public class Perseverance extends Dota2Item {

	public Perseverance(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setHealthRegen(5).setManaRegenPercent(125).setDamage(10));
	}

}
