package hunternif.mc.dota2items.item;


public abstract class TargetItem extends ActiveItem {
	/** [blocks] */
	private double castRange = 5; // Default Minecraft reach

	public TargetItem(int id) {
		super(id);
	}
	
	public double getCastRange() {
		return castRange;
	}
	public TargetItem setCastRange(double value) {
		this.castRange = value;
		return this;
	}
}
