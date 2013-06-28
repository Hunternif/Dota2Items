package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.effect.Effect;
import hunternif.mc.dota2items.effect.EffectInstance;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class EffectPacket extends CustomPacket {
	private int effectID;
	private Object[] data;
	
	public EffectPacket() {}
	
	public EffectPacket(EffectInstance instance) {
		effectID = instance.effectID;
		data = instance.data;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeShort(effectID);
		Effect.effectList[effectID].writeInstanceData(data, out);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		effectID = in.readShort();
		data = Effect.effectList[effectID].readInstanceData(in);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			EffectInstance effInst = new EffectInstance(effectID, data);
			effInst.perform();
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}

}
