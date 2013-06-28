package hunternif.mc.dota2items.effect;

public class EffectInstance {
	public final int effectID;
	public Object[] data;
	
	public EffectInstance(int effectID, Object ... data) {
		this.effectID = effectID;
		this.data = data;
	}
	public EffectInstance(Effect effect, Object ... data) {
		this(effect.id, data);
	}
	
	public Effect getEffect() {
		return Effect.effectList[effectID];
	}
	
	public void perform() {
		getEffect().perform(this);
	}
}
