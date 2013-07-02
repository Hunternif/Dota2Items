package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;

@Description("Multiple instances of spell resistance from items do not stack.")
public class Cloak extends Dota2Item {

	public Cloak(int id) {
		super(id);
		passiveBuff = new Buff(this).setSpellResistance(15);
		setPrice(550);
	}

}
