package hunternif.mc.dota2items.render;

import hunternif.mc.dota2items.model.ModelShopkeeper;
import net.minecraft.client.renderer.entity.RenderLiving;

public class RenderShopkeeper extends RenderLiving {

	public RenderShopkeeper() {
		super(new ModelShopkeeper(), 0.5f);
	}

}
