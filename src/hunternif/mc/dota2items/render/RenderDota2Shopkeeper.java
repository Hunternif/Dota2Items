package hunternif.mc.dota2items.render;

import hunternif.mc.dota2items.model.ModelDota2Shopkeeper;
import net.minecraft.client.renderer.entity.RenderLiving;

public class RenderDota2Shopkeeper extends RenderLiving {

	public RenderDota2Shopkeeper() {
		super(new ModelDota2Shopkeeper(), 0.5f);
	}

}
