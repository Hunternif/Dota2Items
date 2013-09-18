package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.Sound;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.event.UseItemEvent;
import hunternif.mc.dota2items.util.IntVec3;
import hunternif.mc.dota2items.util.MCConstants;
import hunternif.mc.dota2items.util.SideHit;
import hunternif.mc.dota2items.util.TreeUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Tango extends Dota2Item {
	public static final float duration = 16f;
	
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
		} else if (block == Block.wood || block == Block.leaves) {
			IntVec3 trunkBase = null;
			if (block == Block.wood) {
				// Supposedly hit the trunk
				trunkBase = new IntVec3(x, TreeUtil.getTreeTrunkBaseY(world, x, y, z), z);
			} else {
				// Hit some leaves. Looking for the closest tree trunk around
				trunkBase = TreeUtil.findTreeTrunkInBox(world, x, y, z, trunkSearchDeltaY, trunkSearchRadius);
			}
			if (trunkBase != null && trunkBase.y > 0) {
				// Yep, found a tree
				MinecraftForge.EVENT_BUS.post(new UseItemEvent(player, this));
				TreeUtil.removeTree(world, trunkBase, true);
				itemStack.stackSize --;
				EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(player);
				long startTime = world.getTotalWorldTime();
				long endTime = startTime + (long) (duration * MCConstants.TICKS_PER_SECOND);
				stats.addBuff(new BuffInstance(Buff.tango, player.entityId, startTime, endTime, true));
				player.playSound(Sound.TANGO.getName(), 1, 1);
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
