package hunternif.mc.dota2items.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PositionUtil {
	/**
	 * @param borderSize 	extra area to examine around line for entities
	 * @param excluded		any excluded entity
	 * @return a MovingObjectPosition of either the block hit (no entity hit),
	 *         the entity hit (hit an entity), or null for nothing hit.
	 */
	public static MovingObjectPosition tracePath(World world,
			double startX, double startY, double startZ,
			double endX, double endY, double endZ,
			double borderSize, Entity excluded, boolean checkWater) {
		Vec3 startVec = world.getWorldVec3Pool().getVecFromPool(startX, startY, startZ);
		Vec3 lookVec = world.getWorldVec3Pool().getVecFromPool(endX - startX, endY - startY, endZ - startZ);
		Vec3 endVec = world.getWorldVec3Pool().getVecFromPool(endX, endY, endZ);
		double minX = startX < endX ? startX : endX;
		double minY = startY < endY ? startY : endY;
		double minZ = startZ < endZ ? startZ : endZ;
		double maxX = startX > endX ? startX : endX;
		double maxY = startY > endY ? startY : endY;
		double maxZ = startZ > endZ ? startZ : endZ;
		AxisAlignedBB bb = AxisAlignedBB.getAABBPool()
				.getAABB(minX, minY, minZ, maxX, maxY, maxZ)
				.expand(borderSize, borderSize, borderSize);
		List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(excluded, bb);
		MovingObjectPosition blockHit = world.clip(startVec, endVec, checkWater);
		startVec = world.getWorldVec3Pool().getVecFromPool(startX, startY, startZ);
		endVec = world.getWorldVec3Pool().getVecFromPool(endX, endY, endZ);
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
