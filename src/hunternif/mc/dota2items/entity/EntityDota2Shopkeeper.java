package hunternif.mc.dota2items.entity;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.EntityAIMoveTwardsRestriction;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityDota2Shopkeeper extends EntityCreature implements INpc, IInvulnerableEntity {
	private int randomTickDivider;
	public Village villageObj;
	
	
	public EntityDota2Shopkeeper(World world) {
		super(world);
		this.moveSpeed = 0.1F;
		this.setSize(0.6F, 1.8F);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIMoveTwardsRestriction(this, 0.25F));
		this.tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(3, new EntityAIWander(this, 0.25F));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	@Override
	public int getMaxHealth() {
		return 100;
	}
	
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
				this.detachHome();
			}
			else {
				ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
				this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
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
	public String getTexture() {
		return "/mods/"+Dota2Items.ID+"/textures/mob/shopkeeper.png";
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
}
