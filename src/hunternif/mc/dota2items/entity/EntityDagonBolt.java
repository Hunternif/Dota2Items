package hunternif.mc.dota2items.entity;

import hunternif.mc.dota2items.client.particle.ParticleDagon;
import hunternif.mc.dota2items.util.Vec3Chain;
import hunternif.mc.dota2items.util.Vec3Chain.Segment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDagonBolt extends Entity {
	private static final String TAG_END_X = "D2IDagonBoltEndX";
	private static final String TAG_END_Y = "D2IDagonBoltEndY";
	private static final String TAG_END_Z = "D2IDagonBoltEndZ";
	
	public static int maxAge = 8;
	
	public double endX;
	public double endY;
	public double endZ;
	
	public int level;
	
	// -------- Renderer stuff --------
	public Vec3Chain chain;
	public float innerAlpha;
	public double innerWidth;
	public float outerAlpha;
	public double outerWidth;

	public EntityDagonBolt(World world) {
		super(world);
		ignoreFrustumCheck = true;
	}
	
	public EntityDagonBolt(World world, int level,
			double startX, double startY, double startZ,
			double endX, double endY, double endZ) {
		super(world);
		this.level = level;
		setPosition(startX, startY, startZ);
		setEndCoords(endX, endY, endZ);
	}
	
	public void setEndCoords(double endX, double endY, double endZ) {
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
		if (this.worldObj.isRemote) {
			chain = new Vec3Chain(posX, posY, posZ, endX, endY, endZ);
			chain.subdivide(0.3, 0.2, 0.3, 0.2);
			for (Segment seg : chain.segments) {
				Vec3 segVec = seg.getVector();
				int particlesPerSegment = rand.nextInt(level*2);
				for (int i = 0; i < particlesPerSegment; i++) {
					ParticleDagon particle = new ParticleDagon(worldObj,
							seg.start.xCoord + segVec.xCoord * rand.nextDouble(),
							seg.start.yCoord + segVec.yCoord * rand.nextDouble(),
							seg.start.zCoord + segVec.zCoord * rand.nextDouble());
					Minecraft.getMinecraft().effectRenderer.addEffect(particle);
				}
			}
		}
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > maxAge) {
			setDead();
		}
		float ageFraq = ((float)ticksExisted) / (float)maxAge;
		innerWidth = (double)level * 0.025 * (1d - ageFraq);
		outerWidth = innerWidth * 4;
		innerAlpha = 0.3f;
		outerAlpha = 0.2f;
		if (ageFraq > 0.7) {
			innerAlpha *= 1f - (ageFraq - 0.7f)/0.3f;
			outerAlpha *= 1f - (ageFraq - 0.7f)/0.3f;
		}
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		setEndCoords(tag.getDouble(TAG_END_X), tag.getDouble(TAG_END_Y), tag.getDouble(TAG_END_Z));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setDouble(TAG_END_X, endX);
		tag.setDouble(TAG_END_Y, endY);
		tag.setDouble(TAG_END_Z, endZ);
	}
}
