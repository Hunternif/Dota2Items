package hunternif.mc.dota2items.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class NetworkUtil {
	public static int radius = 256;
	
	/** Send effect packets to other players in an area of 512*512 blocks. */
	public static void sendToAllAround(Packet packet, Entity entity) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			server.getConfigurationManager().sendToAllNear(
					entity.posX, entity.posY, entity.posZ, radius, entity.dimension,
					packet);
		}
	}
	
	/** Send effect packets to all players other than the specified player
	 * within the radius of 256 blocks of the given entity. */
	public static void sendToAllAroundExcept(Packet packet, Entity entity, EntityPlayer except) {
		sendToAllAroundExcept(packet, entity.posX, entity.posY, entity.posZ, entity.dimension, except);
	}
	
	/** Send effect packets to all players other than the specified player
	 * within the radius of 256 blocks of specified coordinates. */
	public static void sendToAllAroundExcept(Packet packet, double x, double y, double z, int dimension, EntityPlayer except) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			server.getConfigurationManager().sendToAllNearExcept(except,
					x, y, z, radius, dimension,
					packet);
		}
	}
}
