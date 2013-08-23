package hunternif.mc.dota2items.network;

import hunternif.mc.dota2items.item.CooldownItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class UseItemPacket extends CustomPacket {
	public UseItemPacket(){}

	@Override
	public void write(ByteArrayDataOutput out) {}

	@Override
	public void read(ByteArrayDataInput in) {}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if (!side.isClient()) {
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack.getItem() instanceof CooldownItem) {
				((CooldownItem)stack.getItem()).onClientUsedItem(player, stack);
			}
		} else {
			throw new ProtocolException("Cannot send this packet to the client!");
		}
	}

}
