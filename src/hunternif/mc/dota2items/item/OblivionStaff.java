package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;

@Recipe(ingredients={Quarterstaff.class, SagesMask.class, RobeOfTheMagi.class})
public class OblivionStaff extends Dota2Item {

	public OblivionStaff(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setIntelligence(6).setAttackSpeed(10).setDamage(15).setManaRegenPercent(75));
		weaponDamage = 2;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
