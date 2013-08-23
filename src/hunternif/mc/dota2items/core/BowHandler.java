package hunternif.mc.dota2items.core;

import hunternif.mc.dota2items.Dota2Items;

import java.util.logging.Level;

import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import cpw.mods.fml.common.FMLLog;

public class BowHandler {
	public static final int MAX_CHARGE = 20;
	public static final float TICKS_PER_SECOND = 20;
	
	@ForgeSubscribe
	public void onArrowLoose(ArrowLooseEvent event) {
		EntityStats stats = Dota2Items.mechanics.getEntityStats(event.entityPlayer);
		float speed = (float) MAX_CHARGE / stats.getAttackTime();
		float timeCharged = (float) event.charge / TICKS_PER_SECOND;
		int newCharge = MathHelper.ceiling_float_int( speed * timeCharged );
		FMLLog.log(Dota2Items.ID, Level.INFO, "Changed bow charge from %d to %d", event.charge, newCharge);
		event.charge = newCharge;
	}
}
