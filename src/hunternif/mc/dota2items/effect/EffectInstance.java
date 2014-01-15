package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.network.EffectPacket;
import hunternif.mc.dota2items.util.NetworkUtil;
import net.minecraft.entity.Entity;

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
	
	/** Send effect packets to other players in an area of 512*512 blocks. */
	public static void notifyPlayersAround(EffectInstance effect, Entity entity) {
		NetworkUtil.sendToAllAround(new EffectPacket(effect).makePacket(), entity);
	}
}
