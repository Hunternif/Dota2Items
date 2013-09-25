package hunternif.mc.dota2items.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.Vec3;

public class Vec3Chain {
	public final List<Segment> segments = new ArrayList<Vec3Chain.Segment>();
	public final List<Vec3> coords = new ArrayList<Vec3>();
	
	private boolean coordsRefsChanged = false;
	private Random rand;
	
	public Vec3Chain(double startX, double startY, double startZ,
			double endX, double endY, double endZ) {
		rand = new Random(Double.doubleToLongBits(startX));
		coords.add(Vec3.createVectorHelper(startX, startY, startZ));
		coords.add(Vec3.createVectorHelper(endX, endY, endZ));
		resetSegments();
	}
	
	public void subdivide(double segmentLength, double jitterX, double jitterY, double jitterZ) {
		for (int i = 1; i < coords.size()-1; i++) {
			coords.remove(i);
		}
		resetSegments();
		Segment whole = segments.get(0);
		double length = whole.getLength();
		double divisions = Math.floor(length/segmentLength);
		double actualSegmentLength = length / divisions;
		Vec3 segVec = whole.getVector().normalize();
		segVec.xCoord *= actualSegmentLength;
		segVec.yCoord *= actualSegmentLength;
		segVec.zCoord *= actualSegmentLength;
		for (double r = 1; r < divisions; r++) {
			Vec3 division = whole.start.addVector(
					r * segVec.xCoord + (rand.nextDouble()*2-1) * jitterX,
					r * segVec.yCoord + (rand.nextDouble()*2-1) * jitterY,
					r * segVec.zCoord + (rand.nextDouble()*2-1) * jitterZ);
			coords.add(coords.size()-1, division);
		}
		coordsRefsChanged = true;
		updateSegments();
	}
	
	private void updateSegments() {
		if (coordsRefsChanged) {
			resetSegments();
		} else {
			for (Segment seg : segments) {
				seg.update();
			}
		}
	}
	private void resetSegments() {
		segments.clear();
		for (int i = 0; i < coords.size()-1; i++) {
			segments.add(new Segment(this, coords.get(i), coords.get(i+1)));
		}
		coordsRefsChanged = false;
	}
	
	public static class Segment {
		private Vec3Chain chain;
		public Vec3 start;
		public Vec3 end;
		private double length;
		private float yaw;
		private float pitch;
		public Segment(Vec3Chain chain, Vec3 start, Vec3 end) {
			this.chain = chain;
			this.start = start;
			this.end = end;
			update();
		}
		public double getLength() {
			return length;
		}
		public float getYaw() {
			return yaw;
		}
		public float getPitch() {
			return pitch;
		}
		protected void update() {
			length = Math.sqrt(
					(end.xCoord - start.xCoord)*(end.xCoord - start.xCoord) +
					(end.yCoord - start.yCoord)*(end.yCoord - start.yCoord) +
					(end.zCoord - start.zCoord)*(end.zCoord - start.zCoord));
			double lengthXZ = Math.sqrt(
					(end.xCoord - start.xCoord)*(end.xCoord - start.xCoord) +
					(end.zCoord - start.zCoord)*(end.zCoord - start.zCoord));
			pitch = (float)(Math.atan2(end.yCoord - start.yCoord, lengthXZ) / Math.PI * 180);
			yaw = (float)(-Math.atan2(end.zCoord - start.zCoord, end.xCoord - start.xCoord) / Math.PI * 180);
		}
		public Vec3 getVector() {
			// Because "subtract" is client-side-only for some reason.
			return end.addVector(-start.xCoord, -start.yCoord, -start.zCoord);
		}
	}
}
