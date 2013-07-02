package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import net.minecraft.entity.Entity;

public class BladesOfAttack extends Dota2Item {

	public BladesOfAttack(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(9);
		setPrice(450);
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 5;
	}

}
