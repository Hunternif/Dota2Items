package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.SideHit;
import hunternif.mc.dota2items.util.TreeUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class Tango extends Dota2Item {
	public static final String NAME = "tango";
	
	public static final int duration = 20*16; //16 seconds
	public static final int amplifier = 0;
	
	/** Look for the tree trunk minus this delta on the Y axis from the click point. */
	public static final int trunkSearchDeltaY = 3;
	/** Look for the tree trunk in a horizontal radius from the click point. */
	public static final int trunkSearchRadius = 3;
	
	public Tango(int id) {
		super(id);
		setMaxStackSize(64);
		setUnlocalizedName(NAME);
		setPrice(30);
		//TODO make Tango only available in a set of 3 in the shop.
	}
	
	@Override
	public boolean isFull3D() {
        return false;
    }
	
	//TODO make the custom potion effect particles (namely falling green leaves)
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!canUseItem(player)) {
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
				world.playSoundEffect(x, trunkBaseY, z, Dota2ItemSounds.TREE_FALL, 1.0f, 1.0f);
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
				world.playSoundEffect(trunkBase.x, trunkBase.y, trunkBase.z, Dota2ItemSounds.TREE_FALL, 1.0f, 1.0f);
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
		if (!canUseItem(player)) {
			return itemStack;
		}
		playDenyGeneralSound(world);
		return itemStack;
	}
}
