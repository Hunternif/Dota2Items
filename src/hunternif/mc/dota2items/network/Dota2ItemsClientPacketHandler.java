package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.effect.Dota2Effect;
import hunternif.mc.dota2items.mechanics.buff.BuffInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class Dota2ItemsClientPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(Dota2Items.CHANNEL)) {
			
			// Try parsing effect
			Dota2Effect effect = Dota2Effect.fromPacket(packet);
			if (effect != null) {
				effect.start();
				return;
			}
			
			// Try parsing buff
			BuffInstance buffInst = BuffInstance.fromPacket(packet);
			if (buffInst != null) {
				Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(buffInst.entityID);
				if (entity != null) {
					EntityStats stats = Dota2Items.mechanics.entityStats.get(entity);
					if (stats != null) {
						stats.addBuff(buffInst);
					} else {
						stats = new EntityStats();
						stats.addBuff(buffInst);
						Dota2Items.mechanics.entityStats.put(entity, stats);
					}
				}
				return;
			}
			
			//try parsing entity move
			EntityMovePacket.parseAndApplyEntityMove(packet);
		}
	}
}
