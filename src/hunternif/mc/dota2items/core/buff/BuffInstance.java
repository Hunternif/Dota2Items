package hunternif.mc.dota2items.core.buff;

import net.minecraft.nbt.NBTTagCompound;

public class BuffInstance {
	private static final String TAG_BUFF_ID = "buffId";
	private static final String TAG_END_TIME = "endTime";
	
	public int entityID;
	public Buff buff;
	public long endTime;
	public boolean isItemPassiveBuff;
	
	public BuffInstance(Buff buff, int entityID, long endTime) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = endTime;
	}
	
	public BuffInstance(Buff buff, int entityID) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = -1;
	}
	
	public BuffInstance(Buff buff, int entityID, long endTime, boolean isItemPassiveBuff) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = endTime;
		this.isItemPassiveBuff = isItemPassiveBuff;
	}
	
	public BuffInstance(Buff buff, int entityID, boolean isItemPassiveBuff) {
		this.buff = buff;
		this.entityID = entityID;
		this.endTime = -1;
		this.isItemPassiveBuff = isItemPassiveBuff;
	}
	
	public boolean isPermanent() {
		return endTime < 0;
	}
	
	@Override
	public String toString() {
		return "{"+buff.toString()+"}";
	}
	
	public static BuffInstance fromNBT(NBTTagCompound tag, int entityId) {
		int buffId = tag.getInteger(TAG_BUFF_ID);
		long endTime = tag.getLong(TAG_END_TIME);
		return new BuffInstance(Buff.buffList[buffId], entityId, endTime);
	}
	
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(TAG_BUFF_ID, buff.id);
		tag.setLong(TAG_END_TIME, endTime);
		return tag;
	}
}