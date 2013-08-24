package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.PlayerAttackedHandler;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class PlayerHurtPacket extends EntityStatsSyncPacket {

	@Override
	public void write(ByteArrayDataOutput out) {
		super.write(out);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		super.read(in);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		super.execute(player, side);
		PlayerAttackedHandler.onPlayerHurt(player);
	}

}
