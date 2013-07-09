package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

@Recipe(ingredients={TalismanOfEvasion.class, Quarterstaff.class, Eaglesong.class})
public class Butterfly extends Dota2Item {

	public Butterfly(int id) {
		super(id);
		passiveBuff = new Buff(this).setAgility(30).setDamage(30).setEvasionPercent(35).setAttackSpeed(30);
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 7;
	}

}
