package hunternif.mc.dota2items.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PositionUtil {
	/**
	 * 
	 * @param world
	 * @param x startX
	 * @param y startY
	 * @param z startZ
	 * @param tx endX
	 * @param ty endY
	 * @param tz endZ
	 * @param borderSize 	extra area to examine around line for entities
	 * @param excluded		any excluded entities (the player, etc)
	 * @return a MovingObjectPosition of either the block hit (no entity hit),
	 *         the entity hit (hit an entity), or null for nothing hit
	 */
	//FIXME rewrite this, because it doesn't work the way i want
	public static MovingObjectPosition tracePath(World world, double x, double y,
			double z, double tx, double ty, double tz, double borderSize,
			Entity excluded, boolean checkWater) {
		Vec3 startVec = world.getWorldVec3Pool().getVecFromPool(x, y, z);
		Vec3 lookVec = world.getWorldVec3Pool().getVecFromPool(tx - x, ty - y, tz - z);
		Vec3 endVec = world.getWorldVec3Pool().getVecFromPool(tx, ty, tz);
		double minX = x < tx ? x : tx;
		double minY = y < ty ? y : ty;
		double minZ = z < tz ? z : tz;
		double maxX = x > tx ? x : tx;
		double maxY = y > ty ? y : ty;
		double maxZ = z > tz ? z : tz;
		AxisAlignedBB bb = AxisAlignedBB.getAABBPool()
				.getAABB(minX, minY, minZ, maxX, maxY, maxZ)
				.expand(borderSize, borderSize, borderSize);
		List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(excluded, bb);
		MovingObjectPosition blockHit = world.clip(startVec, endVec, checkWater);
		startVec = world.getWorldVec3Pool().getVecFromPool(x, y, z);
		endVec = world.getWorldVec3Pool().getVecFromPool(tx, ty, tz);
		double maxDistance = endVec.distanceTo(startVec);
		if (blockHit != null) {
			maxDistance = blockHit.hitVec.distanceTo(startVec);
		}
		Entity closestHitEntity = null;
		double closestHit = maxDistance;
		double currentHit = 0.f;
		AxisAlignedBB entityBb;
		MovingObjectPosition intercept;
		for (Entity ent : allEntities) {
			if (ent.canBeCollidedWith()) {
				double entBorder = ent.getCollisionBorderSize();
				entityBb = ent.boundingBox;
				if (entityBb != null) {
					entityBb = entityBb.expand(entBorder, entBorder, entBorder);
					intercept = entityBb.calculateIntercept(startVec, endVec);
					if (intercept != null) {
						currentHit = intercept.hitVec.distanceTo(startVec);
						if (currentHit < closestHit || currentHit == 0) {
							closestHit = currentHit;
							closestHitEntity = ent;
						}
					}
				}
			}
		}
		if (closestHitEntity != null) {
			blockHit = new MovingObjectPosition(closestHitEntity);
		}
		return blockHit;
	}
}
