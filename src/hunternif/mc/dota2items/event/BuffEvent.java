package hunternif.mc.dota2items.event;

import hunternif.mc.dota2items.core.buff.BuffInstance;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BuffEvent extends LivingEvent {
	public BuffInstance buffInst;
	
	public BuffEvent(BuffInstance buffInst, EntityLivingBase entity) {
		super(entity);
		this.buffInst = buffInst;
	}

	public static class BuffAddEvent extends BuffEvent {
		public BuffAddEvent(BuffInstance buffInst, EntityLivingBase entity) {
			super(buffInst, entity);
		}
	}
	
	public static class BuffRemoveEvent extends BuffEvent {
		public BuffRemoveEvent(BuffInstance buffInst, EntityLivingBase entity) {
			super(buffInst, entity);
		}
	}
}
