package hunternif.mc.dota2items.client.particle;

import hunternif.mc.dota2items.Dota2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Dota2Particle extends EntityFX {
	protected static final ResourceLocation dota2itemsParticles = new ResourceLocation(Dota2Items.ID+":textures/particles.png");
	/** This sprite sheet must be strictly 16 by 16 icons. */
	protected static final ResourceLocation minecraftParticles = new ResourceLocation("textures/particle/particles.png");
	private static float ICON_U_WIDTH = 1f/16f;
	
	private int iconStages;
	private int initialIconIndex;
	private float fadeInTime = 0;
	private float baseAlpha = 1;
	private float fadeOutTime = 0.8f;
	private boolean glowing = false;

	public Dota2Particle(World world, double x, double y, double z) {
		this(world, x, y, z, 0, 0, 0);
	}
	
	public Dota2Particle(World world, double x, double y, double z,
			double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.motionX = velX;
		this.motionY = velY;
		this.motionZ = velZ;
	}
	
	protected void setRandomScale(float min, float max) {
		particleScale = rand.nextFloat()*(max-min) + min;
	}
	
	protected void setRandomMaxAge(int min, int max) {
		particleMaxAge = MathHelper.floor_float(rand.nextFloat()*(float)(max-min)) + min;
	}
	
	protected void randomizeVelocity(double maximum) {
		this.motionX += (rand.nextDouble()*2-1) * maximum;
		this.motionY += (rand.nextDouble()*2-1) * maximum;
		this.motionZ += (rand.nextDouble()*2-1) * maximum;
	}
	
	protected void setTexturePositions(int x, int y) {
		setTexturePositions(x, y, 1, false);
	}
	protected void setTexturePositions(int x, int y, int stages, boolean randomStart) {
		if (randomStart) {
			int firstStage = rand.nextInt(stages-1);
			x += firstStage;
			stages -= firstStage;
			if (x >= 16) {
				y++;
				x %= 16;
			}
		}
		initialIconIndex = y * 16 + x;
		iconStages = stages;
		setParticleTextureIndex(initialIconIndex);
	}
	
	protected void setFade(float fadeInTime, float fadeOutTime) {
		this.fadeInTime = fadeInTime;
		this.fadeOutTime = fadeOutTime;
	}
	
	protected void setGlowing() {
		glowing = true;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		// Apparently this defeats the purpose of Tessellator, but it's the only
		// way I found to render custom particle texture without interfering with
		// vanilla particles.
		//NOTE think of a way to optimize custom particle rendering
		if (tessellator.isDrawing) {
			tessellator.draw();
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(dota2itemsParticles);
		tessellator.startDrawingQuads();
		tessellator.setBrightness(getBrightnessForRender(partialTick));
		super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
		tessellator.draw();
		tessellator.startDrawingQuads();
		Minecraft.getMinecraft().renderEngine.bindTexture(minecraftParticles);
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		float ageFraq = ((float)this.particleAge) / (float)this.particleMaxAge;
		
		setAlphaF(alphaAtAge(ageFraq));
		
		particleScale = scaleAtAge(ageFraq);
		
		if (iconStages > 1) {
			int stage = MathHelper.floor_float(ageFraq * (float) iconStages);
			setParticleTextureIndex(initialIconIndex + stage);
		}
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
	
	@Override
	public int getBrightnessForRender(float partialTick) {
		return glowing ? 0xf000f0 : super.getBrightnessForRender(partialTick);
	}
	
	/** Particle alpha as function of particle age (from 0 to 1). */
	protected float alphaAtAge(float ageFraq) {
		if (ageFraq <= fadeInTime) {
			return baseAlpha * ageFraq / fadeInTime;
		} else if (ageFraq >= fadeOutTime) {
			return baseAlpha * (1f - (ageFraq - fadeOutTime) / (1f-fadeOutTime) );
		} else {
			return baseAlpha;
		}
	}
	
	/** Particle scale as function of particle age (from 0 to 1). */
	protected float scaleAtAge(float ageFraq) {
		return particleScale;
	}
}
