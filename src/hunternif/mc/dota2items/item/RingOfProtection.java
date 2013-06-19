package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class RingOfProtection extends Dota2Item {
	public static final String NAME = "ringOfProtection";

	public RingOfProtection(int id) {
		super(id);
		setUnlocalizedName(NAME);
		passiveBuff = Buff.ringOfProtection;
		setPrice(175);
	}

}
