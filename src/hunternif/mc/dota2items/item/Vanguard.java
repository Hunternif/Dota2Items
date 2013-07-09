package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;

@Description("Passive: Damage Block - Gives a chance to block damage, depending on the type of hero you are.")
@Recipe(ingredients={RingOfHealth.class, VitalityBooster.class, StoutShield.class})
public class Vanguard extends Dota2Item {

	public Vanguard(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealth(250).setHealthRegen(6).setDamageBlock(40, 20, 70);
	}

}
