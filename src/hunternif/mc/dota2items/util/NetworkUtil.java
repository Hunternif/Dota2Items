package hunternif.mc.dota2items.util;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class NetworkUtil {
	/** Send effect packets to other players in an area of 512*512 blocks. */
	public static void sendToAllAround(Packet packet, Entity entity) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			server.getConfigurationManager().sendToAllNear(
					entity.posX, entity.posY, entity.posZ, 256, entity.dimension,
					packet);
		}
	}
}
