package hunternif.mc.dota2items.item;

import net.minecraft.util.EnumChatFormatting;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;

@Description("Flat movement speed bonuses from multiple pairs of boots do not stack.")
public class BootsOfSpeed extends Dota2Item {

	public BootsOfSpeed(int id) {
		super(id);
		setPassiveBuff(new Buff(this).setMovementSpeed(50));
		setPrice(450);
	}

}
