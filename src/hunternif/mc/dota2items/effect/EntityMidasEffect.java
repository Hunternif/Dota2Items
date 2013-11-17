package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.entity.TemporaryEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityMidasEffect extends TemporaryEntity {

	public EntityMidasEffect(World world) {
		super(world);
		ignoreFrustumCheck = true;
	}
	
	public EntityMidasEffect(World world, Entity entity) {
		this(world);
		setPosition(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public int getDuration() {
		return 20;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

}
