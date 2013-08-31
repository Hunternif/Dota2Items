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
	public float baseStrength;
	public float baseAgility;
	public float baseIntelligence;
	private float partialHalfHeart;
	private float mana;
	private float gold;
	
	public EntityStatsSyncPacket() {}
	
	public EntityStatsSyncPacket(EntityStats stats) {
		entityID = stats.entityId;
		partialHalfHeart = stats.partialHalfHeart;
		mana = stats.getFloatMana();
		gold = stats.getFloatGold();
		baseStrength = stats.getFloatBaseStrength();
		baseAgility = stats.getFloatBaseAgility();
		baseIntelligence = stats.getFloatBaseIntelligence();
	}
	
	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeInt(entityID);
		out.writeFloat(partialHalfHeart);
		out.writeFloat(mana);
		out.writeFloat(gold);
		out.writeFloat(baseStrength);
		out.writeFloat(baseAgility);
		out.writeFloat(baseIntelligence);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		entityID = in.readInt();
		partialHalfHeart = in.readFloat();
		mana = in.readFloat();
		gold = in.readFloat();
		baseStrength = in.readFloat();
		baseAgility = in.readFloat();
		baseIntelligence =in.readFloat();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (side.isClient()) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			if (entity != null && entity instanceof EntityLivingBase) {
				EntityStats stats = Dota2Items.mechanics.getOrCreateEntityStats((EntityLivingBase)entity);
				stats.partialHalfHeart = partialHalfHeart;
				stats.setGold(gold);
				stats.setMana(mana);
				stats.setBaseStrength(baseStrength);
				stats.setBaseAgility(baseAgility);
				stats.setBaseIntelligence(baseIntelligence);
				stats.lastSyncTime = entity.ticksExisted;
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}
}
