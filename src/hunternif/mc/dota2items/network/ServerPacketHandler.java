package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(Dota2Items.CHANNEL)) {
			
			if (OpenGuiPacket.parseAndApplyOpenGuiPacket(packet, (EntityPlayer) player)) {
				return;
			} else if (ShopFilterInputPacket.parseAndApply(packet, (EntityPlayer) player)) {
				return;
			}
			
		}
	}
}
