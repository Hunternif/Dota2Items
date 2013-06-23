package hunternif.mc.dota2items.block;

import hunternif.mc.dota2items.tileentity.TileEntityCyclone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockCycloneContainer extends BlockContainer {

	public BlockCycloneContainer(int id) {
		super(id, Material.air);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F);
		setBlockUnbreakable();
		setResistance(6000000.0F);
	}
	
	//NOTE Some of these overrides may be unnecessary
	
	@Override
	public boolean getBlocksMovement(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
		return true;
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCyclone();
	}

	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
	}
}
