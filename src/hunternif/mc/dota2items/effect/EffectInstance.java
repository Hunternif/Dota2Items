package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.network.Dota2PacketID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.network.packet.Packet250CustomPayload;

public class EffectInstance {
	public final int effectID;
	public double x;
	public double y;
	public double z;
	public Object[] data;
	
	public EffectInstance(int effectID, double x, double y, double z, Object ... data) {
		this.effectID = effectID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}
	public EffectInstance(Effect effect, double x, double y, double z, Object ... data) {
		this(effect.id, x, y, z, data);
	}
	public EffectInstance(int effectID, double x, double y, double z) {
		this(effectID, x, y, z, (Object) null);
	}
	public EffectInstance(Effect effect, double x, double y, double z) {
		this(effect.id, x, y, z, (Object) null);
	}
	
	public Effect getEffect() {
		return Effect.effectList[effectID];
	}
	
	public void perform() {
		getEffect().perform(this);
	}
	
	public Packet250CustomPayload toPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(Dota2PacketID.EFFECT);
			outputStream.writeChar(effectID);
			outputStream.writeFloat((float)x);
			outputStream.writeFloat((float)y);
			outputStream.writeFloat((float)z);
			if (getEffect().hasAdditionalData()) {
				getEffect().writeAdditionalInstanceData(this, outputStream);
			}
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Dota2Items.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;
	}
	
	public static EffectInstance fromPacket(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int effectID;
		double x;
		double y;
		double z;
		Object[] data = null;
		
		try {
			if (inputStream.readInt() != Dota2PacketID.EFFECT)
				return null;
			effectID = inputStream.readChar();
			x = (double) inputStream.readFloat();
			y = (double) inputStream.readFloat();
			z = (double) inputStream.readFloat();
			if (Effect.effectList[effectID].hasAdditionalData()) {
				data = Effect.effectList[effectID].readAdditionalInstanceData(inputStream);
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return new EffectInstance(effectID, x, y, z, data);
	}
}
