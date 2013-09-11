package hunternif.mc.dota2items.core.buff;

public class BuffPoorMansShield extends Buff {
	public BuffPoorMansShield(String name) {
		super(name);
		setAgility(6);
		setDamageBlock(20, 10, 60);
	}

	@Override
	public int getDamageBlock(boolean melee, boolean isHero) {
		if (isHero) {
			return melee ? damageBlockMelee : damageBlockRanged;
		}
		return super.getDamageBlock(melee, isHero);
	}
}
