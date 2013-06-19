package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.core.buff.Buff;

public class BootsOfSpeed extends Dota2Item {
	public static final String NAME = "bootsOfSpeed";

	public BootsOfSpeed(int id) {
		super(id);
		setUnlocalizedName(NAME);
		passiveBuff = Buff.bootsOfSpeed;
		setPrice(450);
	}

}
