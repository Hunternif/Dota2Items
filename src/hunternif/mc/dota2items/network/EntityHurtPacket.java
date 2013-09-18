package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.AttackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class EntityHurtPacket extends CustomPacket {
	private int entityID;
	
	public EntityHurtPacket() {}
	
	public EntityHurtPacket(EntityLivingBase entity) {
		this.entityID = entity.entityId;
	}

	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(entityID);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		entityID = in.readInt();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				Dota2Items.stats.removeBuffsOnHurt((EntityLivingBase)entity);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
