package hunternif.mc.dota2items.client.particle;

import hunternif.mc.dota2items.util.MathUtil;
import hunternif.mc.dota2items.util.SideHit;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXCycloneRing extends EntityFX {
	public static float ascendVelocity = 0.2f;
	public static int puffsPerRing = 12;
	public static float maxRingHeight = 4;
	public static float baseAngleVelocity = 0.4f;
	
	private EntityFX[] puffs;
	private float dAngle;
	
	private float yaw;
	private float pitch;
	private Vec3 axis;
	private float ringHeight;
	private float baseAngle;
	
	public FXCycloneRing(World world, double x, double y, double z, float yaw, float pitch, float alpha, EffectRenderer renderer) {
		super(world, x, y, z, 0, 0, 0);
		axis = Vec3.createVectorHelper(0, 1, 0);
		particleGravity = 0;
		this.yaw = yaw;
		this.pitch = pitch;
		axis.rotateAroundX(pitch);
		axis.rotateAroundY(yaw);
		ringHeight = ascendVelocity; // starts just a bit off the ground
		baseAngle = 0;
		this.particleAlpha = alpha;
		
		puffs = new EntityFX[puffsPerRing];
		for (int i = 0; i < puffsPerRing; i++) {
			if (Math.random() < 0.07) {
				// Add *dust* of the block below instead of the smoke puff
				int xInt = MathHelper.floor_double(x);
				int yInt = MathHelper.floor_double(y) - 1;
				int zInt = MathHelper.floor_double(z);
				int blockId = world.getBlockId(xInt, yInt, zInt);
				if (blockId > 0) {
					int metadata = world.getBlockMetadata(xInt, yInt, zInt);
					// The "y + 0.1" below is a workaround for the bug that digging
					// particles stayed on the ground and didn't fly up for some reason.
					puffs[i] = new EntityDiggingFX(world, x + 0.5, y+0.1, z + 0.5, 0, 0, 0, Block.blocksList[blockId], SideHit.TOP, metadata).applyColourMultiplier(xInt, yInt, zInt).multipleParticleScaleBy(0.5f);
					renderer.addEffect(puffs[i]);
					continue;
				}
			}
			puffs[i] = new ParticleCyclone(world, x + 0.5, y, z + 0.5, 0, 0, 0);
			puffs[i].setAlphaF(particleAlpha);
			renderer.addEffect(puffs[i]);
		}
		dAngle = MathUtil._2_PI / ((float) puffs.length);
	}
	
	@Override
	public void setAlphaF(float alpha) {
		super.setAlphaF(alpha);
		for (int i = 0; i < puffs.length; i++) {
			puffs[i].setAlphaF(alpha);
		}
	}
	
	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {}
	
	@Override
	public void onUpdate() {
		//super.onUpdate();
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
		float ringWidth = getWidthVSHeight(ringHeight);
		for (int i = 0; i < puffs.length; i++) {
			Vec3 vec = worldObj.getWorldVec3Pool().getVecFromPool(ringWidth, 0, 0);
			vec.rotateAroundY(baseAngle + ((float)i)*dAngle);
			vec.rotateAroundX(pitch);
			vec.rotateAroundY(yaw);
			puffs[i].motionX = posX + 0.5 + vec.xCoord - puffs[i].posX;
			puffs[i].motionY = posY + 0.5 + vec.yCoord - puffs[i].posY;
			puffs[i].motionZ = posZ + 0.5 + vec.zCoord - puffs[i].posZ;
		}
		if (axis != null) {
			if (ringHeight < maxRingHeight) {
				//motionX = axis.xCoord * ascendVelocity;
				//motionY = axis.yCoord * ascendVelocity;
				//motionZ = axis.zCoord * ascendVelocity;
				//moveEntity(motionX, motionY, motionZ);
				posX += axis.xCoord * ascendVelocity;
				posY += axis.yCoord * ascendVelocity;
				posZ += axis.zCoord * ascendVelocity;
				ringHeight += ascendVelocity;
			} else {
				for (int i = 0; i < puffsPerRing; i++) {
					puffs[i].setDead();
				}
				setDead();
			}
		}
		baseAngle += baseAngleVelocity;
		if (baseAngle > MathUtil._2_PI) {
			baseAngle -= MathUtil._2_PI;
		}
	}
	
	public static float getWidthVSHeight(float height) {
		return (height*height*height*height)*0.003f + height*0.2f + 0.3f;
	}
}
