package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.client.model.ModelShopkeeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderShopkeeper extends RenderLiving {
	private static final ResourceLocation texture = new ResourceLocation(Dota2Items.ID+":textures/entity/shopkeeper.png");

	public RenderShopkeeper() {
		super(new ModelShopkeeper(), 0.5f);
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return texture;
	}

}
