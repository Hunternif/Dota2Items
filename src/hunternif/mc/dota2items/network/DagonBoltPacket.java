package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.effect.EntityDagonBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class DagonBoltPacket extends CustomPacket {
	private int entityID;
	private int level;
	private double endX;
	private double endY;
	private double endZ;
	
	public DagonBoltPacket() {}
	
	public DagonBoltPacket(EntityDagonBolt bolt) {
		this.entityID = bolt.entityId;
		this.level = bolt.level;
		this.endX = bolt.endX;
		this.endY = bolt.endY;
		this.endZ = bolt.endZ;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(entityID);
		out.writeInt(level);
		out.writeDouble(endX);
		out.writeDouble(endY);
		out.writeDouble(endZ);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		entityID = in.readInt();
		level = in.readInt();
		endX = in.readDouble();
		endY = in.readDouble();
		endZ = in.readDouble();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityDagonBolt) {
				EntityDagonBolt bolt = (EntityDagonBolt) entity;
				bolt.level = level;
				bolt.setEndCoords(endX, endY, endZ);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
