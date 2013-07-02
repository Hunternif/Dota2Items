package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;
import net.minecraft.entity.Entity;

@Description("Passive: Damage Block - Gives a chance to block damage, depending on the type of hero you are.")
public class StoutShield extends Dota2Item {

	public StoutShield(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamageBlock(20, 10, 60);
		setPrice(250);
	}

}
