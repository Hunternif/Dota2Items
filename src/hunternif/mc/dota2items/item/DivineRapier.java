package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;

@Recipe(ingredients={SacredRelic.class, DemonEdge.class})
@Description("Drops on death.")
public class DivineRapier extends Dota2Item {

	public DivineRapier(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(300);
		dropsOnDeath = true;
		weaponDamage = 7;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}

}
