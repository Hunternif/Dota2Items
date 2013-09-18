package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * @author credits to diesieben07
 */
public abstract class CustomPacket {
	private static final BiMap<Integer, Class<? extends CustomPacket>> idMap;
		
	static {
		ImmutableBiMap.Builder<Integer, Class<? extends CustomPacket>> builder = ImmutableBiMap.builder();
		
		builder.put(Integer.valueOf(0), EntityMovePacket.class);
		builder.put(Integer.valueOf(1), EntityStatsSyncPacket.class);
		builder.put(Integer.valueOf(2), BuffPacket.class);
		builder.put(Integer.valueOf(3), EffectPacket.class);
		builder.put(Integer.valueOf(4), OpenGuiPacket.class);
		builder.put(Integer.valueOf(5), ShopSellPacket.class);
		builder.put(Integer.valueOf(6), ShopBuySetResultPacket.class);
		builder.put(Integer.valueOf(7), ShopBuySetFilterPacket.class);
		builder.put(Integer.valueOf(8), ShopBuyScrollPacket.class);
		builder.put(Integer.valueOf(9), EntityHurtPacket.class);
		builder.put(Integer.valueOf(10), BuffForcePacket.class);
		
		idMap = builder.build();
	}

	public static CustomPacket constructPacket(int packetId)
			throws ProtocolException, InstantiationException, IllegalAccessException {
		Class<? extends CustomPacket> clazz = idMap.get(Integer.valueOf(packetId));
		if (clazz == null) {
			throw new ProtocolException("Unknown Packet Id!");
		} else {
			return clazz.newInstance();
		}
	}

	public static class ProtocolException extends Exception {
		public ProtocolException() {
		}
		public ProtocolException(String message, Throwable cause) {
			super(message, cause);
		}
		public ProtocolException(String message) {
			super(message);
		}
		public ProtocolException(Throwable cause) {
			super(cause);
		}
	}

	public final int getPacketId() {
		if (idMap.inverse().containsKey(getClass())) {
			return idMap.inverse().get(getClass()).intValue();
		} else {
			throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
		}
	}
	
	public final Packet makePacket() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeByte(getPacketId());
		write(out);
		return PacketDispatcher.getPacket(Dota2Items.CHANNEL, out.toByteArray());
	}

	public abstract void write(ByteArrayDataOutput out);
	
	public abstract void read(ByteArrayDataInput in) throws ProtocolException;
	
	public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;
}