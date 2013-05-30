package hunternif.mc.dota2items.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ChargedItem extends CooldownItem {
	// Charges are represented by stack sizes.
	
	public ChargedItem(int id) {
		super(id);
	}
	
	public ChargedItem setMaxCharges(int value) {
		setMaxStackSize(value);
		return this;
	}
	public int getMaxCharges() {
		return getItemStackLimit();
	}
	
	/** Depletes 1 charge. Returns false if no charges left or on cooldown. */
	public boolean depleteCharge(ItemStack itemStack) {
		if (isOnCooldown(itemStack)) {
			return false;
		} else {
			if (itemStack.stackSize == 1) {
				return false;
			} else {
				itemStack.stackSize --;
				return true;
			}
		}
	}
}
