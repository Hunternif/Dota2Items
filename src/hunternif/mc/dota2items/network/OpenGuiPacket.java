package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class OpenGuiPacket extends CustomPacket {
	private int guiId;
	
	public OpenGuiPacket() {}
	
	public OpenGuiPacket(int guiId) {
		this.guiId = guiId;
	}
	
	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeShort(guiId);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		guiId = in.readShort();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY);
		int z = MathHelper.floor_double(player.posZ);
		player.openGui(Dota2Items.instance, guiId, player.worldObj, x, y, z);
	}
}
