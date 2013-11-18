package hunternif.mc.dota2items.effect;

import hunternif.mc.dota2items.client.particle.ParticleCoin;
import hunternif.mc.dota2items.entity.TemporaryEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMidasEffect extends TemporaryEntity {
	/** ID of the player who cast Hand of Midas and will gather the coins. */
	private static final int DATA_PLAYER_ID = 29;
	private boolean particlesSpawned = false;
	
	/** The player who will gather the coins. */
	private Entity player;
	
	public EntityMidasEffect(World world) {
		super(world);
		ignoreFrustumCheck = true;
	}
	
	public EntityMidasEffect(World world, EntityPlayer player, Entity entity) {
		this(world);
		setPosition(entity.posX, entity.posY, entity.posZ);
		this.player = player;
		dataWatcher.updateObject(DATA_PLAYER_ID, Integer.valueOf(player.entityId));
	}
	
	@Override
	protected void entityInit() {
		dataWatcher.addObject(DATA_PLAYER_ID, Integer.valueOf(player == null ? -1 : player.entityId));
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (worldObj.isRemote && !particlesSpawned) {
			if (player != null) {
				for (int i = 0; i < 10; i ++) {
					ParticleCoin coin = new ParticleCoin(worldObj,
							posX + (rand.nextDouble()-0.5)*0.3,
							posY + 1 + (rand.nextDouble()-0.5)*0.2,
							posZ + (rand.nextDouble()-0.5)*0.3,
							0, 0.1, 0, player);
					Minecraft.getMinecraft().effectRenderer.addEffect(coin);
				}
				particlesSpawned = true;
			} else {
				int playerID = dataWatcher.getWatchableObjectInt(DATA_PLAYER_ID);
				player = worldObj.getEntityByID(playerID);
			}
		}
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
