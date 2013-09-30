package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.entity.EntityWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class EntityWrapperPacket extends CustomPacket {
	private int wrapperID;
	private int wrappedEntityID;
	
	public EntityWrapperPacket() {}
	
	public EntityWrapperPacket(EntityWrapper wrapper) {
		this.wrapperID = wrapper.entityId;
		this.wrappedEntityID = wrapper.entity == null ? -1 : wrapper.entity.entityId;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(wrapperID);
		out.writeInt(wrappedEntityID);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		wrapperID = in.readInt();
		wrappedEntityID = in.readInt();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(wrapperID);
			if (entity != null && entity instanceof EntityWrapper) {
				EntityWrapper wrapper = (EntityWrapper) entity;
				wrapper.entity = wrapper.worldObj.getEntityByID(wrappedEntityID);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
