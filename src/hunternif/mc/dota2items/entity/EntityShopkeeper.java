package hunternif.mc.dota2items.entity;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.client.gui.GuiHandler;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityShopkeeper extends EntityCreature implements INpc, IInvulnerableEntity {
	private int randomTickDivider;
	public Village villageObj;
	
	
	public EntityShopkeeper(World world) {
		super(world);
		this.setSize(0.6F, 1.8F);
		this.getNavigator().setAvoidsWater(true);
		//this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.4));
		this.tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 3, 1));
		//this.tasks.addTask(3, new EntityAIWander(this, 0.4));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLivingBase.class, 8));
	}
	
	/** Presumably sets movement speed. */
	/*@Override
	protected void func_110147_ax() {
		super.func_110147_ax();
		this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.25);
	}*/
	
	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}
	
	@Override
	public boolean isAIEnabled() {
		return true;
	}
	
	@Override
	protected void updateAITick() {
		if (--this.randomTickDivider <= 0) {
			this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
			
			if (this.villageObj == null) {
				this.func_110177_bN(); // detachHome();
			}
			else {
				ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
				// Set Home area
				this.func_110171_b(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
			}
		}
		super.updateAITick();
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		// Dunno why this is done, probably some kludge.
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean interact(EntityPlayer player) {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.posY);
		int z = MathHelper.floor_double(this.posZ);
		player.openGui(Dota2Items.instance, GuiHandler.GUI_ID_SHOP_BUY, this.worldObj, x, y, z);
		return true;
	}
}
