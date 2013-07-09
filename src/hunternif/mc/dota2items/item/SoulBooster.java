package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

@Recipe(ingredients={VitalityBooster.class, EnergyBooster.class, PointBooster.class})
public class SoulBooster extends Dota2Item {

	public SoulBooster(int id) {
		super(id);
		passiveBuff = new Buff(this).setHealth(450).setMana(400).setHealthRegen(4).setManaRegenPercent(100);
	}

}
