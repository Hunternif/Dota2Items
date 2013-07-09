package hunternif.mc.dota2items.item;

import hunternif.mc.dota2items.Config.Recipe;
import hunternif.mc.dota2items.core.buff.Buff;
import hunternif.mc.dota2items.util.DescriptionBuilder.Description;
import net.minecraft.entity.Entity;

@Recipe(ingredients={SacredRelic.class, DemonEdge.class})
@Description("Drops on death.")
public class DivineRapier extends Dota2Item {

	public DivineRapier(int id) {
		super(id);
		passiveBuff = new Buff(this).setDamage(300);
		dropsOnDeath = true;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public int getDamageVsEntity(Entity entity) {
		return 7;
	}

}
