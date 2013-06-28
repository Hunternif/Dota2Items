package hunternif.mc.dota2items.effect;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Effect {
	public static final Effect[] effectList = new Effect[2];
	public static final Effect blink = new EffectBlink(0);
	public static final Effect cyclone = new EffectCyclone(1);
	
	public final int id;
	
	public Effect(int id) {
		this.id = id;
		effectList[id] = this;
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void perform(EffectInstance inst);
	
	public abstract Object[] readInstanceData(ByteArrayDataInput in);
	
	public abstract void writeInstanceData(Object[] data, ByteArrayDataOutput out);
}
