package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.item.ForceStaff;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class BuffForcePacket extends BuffPacket {
	protected float yaw;
	
	public BuffForcePacket() {}
	
	public BuffForcePacket(BuffInstance inst) {
		super(inst);
		yaw = inst.tag.getFloat(ForceStaff.TAG_YAW);
	}
	
	@Override
	public void write(ByteArrayDataOutput out) {
		super.write(out);
		out.writeFloat(yaw);
	}
	
	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		super.read(in);
		yaw = in.readFloat();
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				EntityStats stats = Dota2Items.stats.getOrCreateEntityStats((EntityLivingBase)entity);
				BuffInstance buffInst = new BuffInstance(Buff.buffList[buffID], (EntityLivingBase)entity, startTime, endTime, isFriendly);
				buffInst.tag.setFloat(ForceStaff.TAG_YAW, yaw);
				stats.addBuff(buffInst);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
