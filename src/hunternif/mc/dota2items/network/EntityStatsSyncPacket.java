package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class EntityStatsSyncPacket extends CustomPacket {
	private int entityID;
	private float partialHalfHeart;
	private float mana;
	private float gold;
	
	public EntityStatsSyncPacket() {}
	
	public EntityStatsSyncPacket(EntityStats stats) {
		entityID = stats.entityId;
		partialHalfHeart = stats.partialHalfHeart;
		mana = stats.getFloatMana();
		gold = stats.getFloatGold();
	}
	
	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(entityID);
		out.writeFloat(partialHalfHeart);
		out.writeFloat(mana);
		out.writeFloat(gold);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		entityID = in.readInt();
		partialHalfHeart = in.readFloat();
		mana = in.readFloat();
		gold = in.readFloat();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				EntityStats stats = Dota2Items.mechanics.getEntityStats((EntityLivingBase)entity);
				stats.partialHalfHeart = partialHalfHeart;
				stats.setGold(gold);
				stats.setMana(mana);
				stats.lastSyncTime = entity.ticksExisted;
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
