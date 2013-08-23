package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.MCConstants;
import hunternif.mc.dota2items.util.SideHit;
import hunternif.mc.dota2items.util.TreeUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Tango extends Dota2Item {

	public static final int duration = MathHelper.ceiling_float_int(MCConstants.TICKS_PER_SECOND * 16f); //16 seconds
	public static final int amplifier = 0;
	
	/** Look for the tree trunk minus this delta on the Y axis from the click point. */
	public static final int trunkSearchDeltaY = 3;
	/** Look for the tree trunk in a horizontal radius from the click point. */
	public static final int trunkSearchRadius = 3;
	
	public Tango(int id) {
		super(id);
		setMaxStackSize(64);
		setDefaultQuantity(3);
	}
	
	@Override
	public boolean isFull3D() {
        return false;
    }
	
	//TODO make the custom potion effect particles (namely falling green leaves)
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!tryUse(itemStack, player)) {
			return false;
		}
		// Looking for a tree
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == Block.vine) {
			// Hit vine while probably aiming at what was behind it:
			switch(side) {
			case SideHit.NORTH:
				x++;
				break;
			case SideHit.SOUTH:
				x--;
				break;
			case SideHit.EAST:
				z++;
				break;
			case SideHit.WEST:
				z--;
				break;
			}
			// Now Try again
			block = Block.blocksList[world.getBlockId(x, y, z)];
		}
		if (block == Block.snow) {
			// Hit snow while probably aiming at what was below it:
			y--;
			// Now Try again
			block = Block.blocksList[world.getBlockId(x, y, z)];
		}
		if (block != Block.wood && block != Block.leaves) {
			return false;
		} else if (block == Block.wood) {
			// Supposedly hit the trunk
			int trunkBaseY = TreeUtil.getTreeTrunkBaseY(world, x, y, z);
			if (trunkBaseY > 0) {
				// Yep, found a tree
				TreeUtil.removeTree(world, new IntVec3(x, trunkBaseY, z), true);
				itemStack.stackSize --;
				world.playSoundEffect(x, trunkBaseY, z, Sound.TREE_FALL.getName(), 1.0f, 1.0f);
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, duration, amplifier));
				return true;
			} else {
				return false;
			}
		} else if (block == Block.leaves) {
			// Hit some leaves. Looking for the closest tree trunk around
			IntVec3 trunkBase = TreeUtil.findTreeTrunkInBox(world, x, y, z, trunkSearchDeltaY, trunkSearchRadius);
			if (trunkBase != null) {
				// Yep, found a tree
				TreeUtil.removeTree(world, trunkBase, true);
				itemStack.stackSize --;
				world.playSoundEffect(trunkBase.x, trunkBase.y, trunkBase.z, Sound.TREE_FALL.getName(), 1.0f, 1.0f);
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, duration, amplifier));
				return true;
			} else {
				return false;
			}
		} else {
			// Dead code
			return false;
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		playDenyGeneralSound(player);
		return itemStack;
	}
}
