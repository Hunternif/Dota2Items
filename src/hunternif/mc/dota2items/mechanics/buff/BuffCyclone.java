package hunternif.mc.dota2items.mechanics.buff;

import hunternif.mc.dota2items.mechanics.Dota2EntityStats;

public class BuffCyclone extends Dota2EntityBuff {
	public BuffCyclone(Dota2EntityStats stats, long timeout) {
		super(CYCLONE, stats, timeout);
	}

	@Override
	public void setStartStats() {
		stats.enableInvulnerable();
		stats.disableAttack();
		stats.disableMove();
		stats.disableItems();
	}
	
	@Override
	public void setEndStats() {
		stats.disableInvulnerable();
		stats.enableAttack();
		stats.enableMove();
		stats.enableItems();
	}
}
