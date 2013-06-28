package hunternif.mc.dota2items.item;

import net.minecraft.util.EnumChatFormatting;
import hunternif.mc.dota2items.core.buff.Buff;

public class BootsOfSpeed extends Dota2Item {

	public BootsOfSpeed(int id) {
		super(id);
		passiveBuff = new Buff(this).setMovementSpeed(50);
		setPrice(450);
		description = "Flat movement speed bonuses from multiple pairs of boots do not stack.";
	}

}
