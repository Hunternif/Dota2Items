package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.util.MCConstants;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class BowHandler {
	public static final int MAX_CHARGE = 20;
	
	@ForgeSubscribe
	public void onArrowLoose(ArrowLooseEvent event) {
		EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats(event.entityPlayer);
		float speed = (float) MAX_CHARGE / stats.getAttackTime();
		float timeCharged = (float) event.charge / MCConstants.TICKS_PER_SECOND;
		int newCharge = MathHelper.ceiling_float_int( speed * timeCharged );
		Dota2Items.logger.severe(String.format("Changed bow charge from %d to %d", event.charge, newCharge));
		event.charge = newCharge;
	}
}
