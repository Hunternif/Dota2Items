package hunternif.mc.dota2items.client.particle;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class ParticleDust extends EntitySmokeFX {

	public ParticleDust(World par1World, double par2, double par4, double par6,
			double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
		this.particleAlpha = 0.5f;
		this.particleRed = this.particleGreen = this.particleBlue = 0.7f + (float)(Math.random() * 0.3);
	}

}
