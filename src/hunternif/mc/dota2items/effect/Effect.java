package hunternif.mc.dota2items.effect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	
	public boolean hasAdditionalData() {
		return false;
	}
	
	public Object[] readAdditionalInstanceData(DataInputStream input) throws IOException {
		return null;
	}
	
	public void writeAdditionalInstanceData(EffectInstance instance, DataOutputStream output) throws IOException {}
}
