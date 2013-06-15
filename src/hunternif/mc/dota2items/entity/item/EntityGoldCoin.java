package hunternif.mc.dota2items.entity.item;

import hunternif.mc.dota2items.Dota2ItemSounds;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityGoldCoin extends Entity {
	public static final int MAX_AGE = 6000;
	private static final String TAG_GOLD_VALUE = "D2IgoldValue";
	private static final String TAG_HEALTH = "health";
	private static final String TAG_AGE = "age";
	
	public int coinAge = 0;
	private int coinHealth = 10;
	private int goldValue;

	public EntityGoldCoin(World world, double x, double y, double z, int goldValue) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
		this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.goldValue = goldValue;
		this.isImmuneToFire = true;
	}
	
	public EntityGoldCoin(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.coinHealth = nbttagcompound.getShort(TAG_HEALTH) & 255;
        this.coinAge = nbttagcompound.getShort(TAG_AGE);
        this.goldValue = nbttagcompound.getShort(TAG_GOLD_VALUE);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort(TAG_HEALTH, (short)((byte)this.coinHealth));
		nbttagcompound.setShort(TAG_AGE, (short)this.coinAge);
		nbttagcompound.setShort(TAG_GOLD_VALUE, (short)this.goldValue);
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if (!this.worldObj.isRemote) {
			this.playSound(Dota2ItemSounds.COINS, 0.8F, 0.8f + this.rand.nextFloat()*0.2f);
			player.onItemPickup(this, 1);
			this.setDead();
		}
		EntityStats stats = Dota2Items.mechanics.getEntityStats(player);
		stats.addGold(goldValue);
	}

	/** Borrowed from EntityXPOrb. */
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.029999999329447746D;

		if (this.worldObj.getBlockMaterial(
				MathHelper.floor_double(this.posX),
				MathHelper.floor_double(this.posY),
				MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = 0.20000000298023224D;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		// The following commented-out code draws this entity to the closest player.
		/*double d0 = 8.0D;

		if (this.xpTargetColor < this.xpColor - 20 + this.entityId % 100) {
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0) {
				this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
			}

			this.xpTargetColor = this.xpColor;
		}

		if (this.closestPlayer != null) {
			double d1 = (this.closestPlayer.posX - this.posX) / d0;
			double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
			double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = 1.0D - d4;

			if (d5 > 0.0D) {
				d5 *= d5;
				this.motionX += d1 / d4 * d5 * 0.1D;
				this.motionY += d2 / d4 * d5 * 0.1D;
				this.motionZ += d3 / d4 * d5 * 0.1D;
			}
		}*/

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f = 0.98F;

		if (this.onGround) {
			f = 0.58800006F;
			int i = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

			if (i > 0) {
				f = Block.blocksList[i].slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)f;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= (double)f;

		if (this.onGround) {
			this.motionY *= -0.8999999761581421D;
		}
		
		coinAge++;

		if (coinAge >= MAX_AGE) {
			setDead();
		}
	}
	
	@Override
	protected void dealFireDamage(int par1) {
		this.attackEntityFrom(DamageSource.inFire, par1);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			this.setBeenAttacked();
			this.coinHealth -= par2;
			if (this.coinHealth <= 0) {
				this.setDead();
			}
			return false;
		}
	}
	
	@Override
	public boolean canAttackWithItem() {
		return false;
	}
	
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	public static int getGoldSplit(int amount) {
		return amount >= 10 ? 10 : amount;
	}
	
	public static void spawnAtEntity(Entity entity, int goldAmount) {
		while (goldAmount > 0) {
			int part = getGoldSplit(goldAmount);
			goldAmount -= part;
			entity.worldObj.spawnEntityInWorld(new EntityGoldCoin(entity.worldObj, entity.posX, entity.posY, entity.posZ, part));
		}
	}
}
