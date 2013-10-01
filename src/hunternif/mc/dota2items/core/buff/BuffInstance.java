package hunternif.mc.dota2items.core.buff;

import hunternif.mc.dota2items.effect.ContinuousEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;


public final class BuffInstance {
	private static final String TAG_BUFF_ID = "buffId";
	private static final String TAG_START_TIME = "startTime";
	private static final String TAG_END_TIME = "endTime";
	private static final String TAG_IS_FRIENDLY = "isFriendly";
	
	public EntityLivingBase entity;
	public Buff buff;
	public long startTime;
	public long endTime;
	public boolean isFriendly;
	public NBTTagCompound tag;
	public ContinuousEffect effect;
	
	/** For passive item buffs. */
	public BuffInstance(Buff buff, EntityLivingBase entity, boolean isFriendly) {
		this(buff, entity, 0, -1, isFriendly);
	}
	
	public BuffInstance(Buff buff, EntityLivingBase entity, long startTime, long endTime, boolean isFriendly) {
		this.buff = buff;
		this.entity = entity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isFriendly = isFriendly;
		this.tag = toNBT();
	}
	
	/** Only passive buffs from items can be permanent. */
	public boolean isPermanent() {
		return endTime < 0;
	}
	
	public boolean isItemPassiveBuff() {
		return isPermanent();
	}
	
	@Override
	public String toString() {
		return "{"+buff.toString()+"}";
	}
	
	public static BuffInstance fromNBT(NBTTagCompound tag, EntityLivingBase entity) {
		int buffId = tag.getInteger(TAG_BUFF_ID);
		long startTime = tag.getLong(TAG_START_TIME);
		long endTime = tag.getLong(TAG_END_TIME);
		boolean isFriendly = tag.getBoolean(TAG_IS_FRIENDLY);
		return new BuffInstance(Buff.buffList[buffId], entity, startTime, endTime, isFriendly);
	}
	
	public NBTTagCompound toNBT() {
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setInteger(TAG_BUFF_ID, buff.id);
		tag.setLong(TAG_START_TIME, startTime);
		tag.setLong(TAG_END_TIME, endTime);
		tag.setBoolean(TAG_IS_FRIENDLY, isFriendly);
		return tag;
	}
	
	public long getDuration() {
		return endTime - startTime;
	}
}