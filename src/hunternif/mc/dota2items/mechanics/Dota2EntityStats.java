package hunternif.mc.dota2items.mechanics;

import hunternif.mc.dota2items.event.EntityImmobilizeEvent;
import hunternif.mc.dota2items.event.EntityUnimmobilizeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;


public class Dota2EntityStats {
	public Entity entity;
	
	private int lockCount_disableAttack = 0;
	private int lockCount_enableMagicImmune = 0;
	private int lockCount_enableInvulnerable = 0;
	private int lockCount_disableMove = 0;
	private int lockCount_disableItems = 0;
	
	public int health = 100;
	public int mana = 0;
	public int movementSpeed = 250;
	
	
	public Dota2EntityStats(Entity entity) {
		this.entity = entity;
	}
	
	public boolean canAttack() {
		return lockCount_disableAttack == 0;
	}
	public void disableAttack() {
		lockCount_disableAttack++;
	}
	public void enableAttack() {
		lockCount_disableAttack--;
	}
	
	public boolean canMove() {
		return lockCount_disableMove == 0;
	}
	public void disableMove() {
		if (canMove()) {
			MinecraftForge.EVENT_BUS.post(new EntityImmobilizeEvent(entity));
		}
		lockCount_disableMove++;
	}
	public void enableMove() {
		lockCount_disableMove--;
		if (canMove()) {
			MinecraftForge.EVENT_BUS.post(new EntityUnimmobilizeEvent(entity));
		}
	}
	
	public boolean isInvulnerable() {
		return lockCount_enableInvulnerable > 0;
	}
	public void disableInvulnerable() {
		lockCount_enableInvulnerable--;
	}
	public void enableInvulnerable() {
		lockCount_enableInvulnerable++;
	}
	
	public boolean isMagicImmune() {
		return lockCount_enableMagicImmune > 0;
	}
	public void disableMagicImmune() {
		lockCount_enableMagicImmune--;
	}
	public void enableMagicImmune() {
		lockCount_enableMagicImmune++;
	}
	
	public boolean canUseItems() {
		return lockCount_disableItems == 0;
	}
	public void disableItems() {
		lockCount_disableItems++;
	}
	public void enableItems() {
		lockCount_disableItems--;
	}
}
