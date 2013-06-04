package hunternif.mc.dota2items.tileentity;

import hunternif.mc.dota2items.effect.Dota2Effect;
import hunternif.mc.dota2items.util.MathUtil;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCyclone extends TileEntity {
	public static final String NAME = "Cyclone";
	public static float maxPitch = 0.4f;
	public static float duration = 2.5f; // seconds
	
	private float elapsed = 0;
	private float alpha = 0.7f;
	
	private float yaw = 0;
	private float yawVelocity = 0.1f;
	private float pitch = 0;
	private static float pitchVelocity = 0;
	
	public TileEntityCyclone() {
		super();
	}
	
	@Override
	public void updateEntity() {
		if (elapsed >= duration) {
			worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
			worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			return;
		}
		elapsed += 0.05f;
		if (this.worldObj.isRemote) {
			yaw += yawVelocity;
			if (yaw > MathUtil._2_PI)
				yaw -= MathUtil._2_PI;
			
			if (Math.random() < 0.1) {
				//if (pitchVelocity < 0.01)
					pitchVelocity = 0.2f;
			}
			pitch += pitchVelocity;
			if (pitch > maxPitch)
				pitch = maxPitch;
			if (pitchVelocity > 0) {
				pitchVelocity -= 0.05f;
			} else {
				pitchVelocity = 0;
			}
			if (pitch > 0) {
				pitch -= 0.07f;
			} else {
				pitch = 0;
			}
			
			//EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
			//CycloneFXRing particle = new CycloneFXRing(worldObj, xCoord, yCoord, zCoord, yaw, pitch, effectRenderer);
			if (duration - elapsed < 0.5f && alpha > 0) {
				alpha -= 0.05f;
			}
			Dota2Effect effect = new Dota2Effect(Dota2Effect.CYCLONE_RING, xCoord, yCoord, zCoord, yaw, pitch, alpha);
			effect.start();
			//particle.setAlphaF(alpha);
			//effectRenderer.addEffect(particle, this);
        }
    }
}
