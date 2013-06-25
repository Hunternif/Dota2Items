package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.inventory.ItemColumn;
import hunternif.mc.dota2items.item.Dota2Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SecretShopItemRenderer implements IItemRenderer {
	private static final int ICON_SIZE = 5;
	private static final RenderItem renderItem = new RenderItem();

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY && itemStack.getItem() instanceof Dota2Item &&
				((Dota2Item)itemStack.getItem()).shopColumn == ItemColumn.COLUMN_SECRET_SHOP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
		if (font == null) {
			font = Minecraft.getMinecraft().fontRenderer;
		}
		RenderEngine engine = Minecraft.getMinecraft().renderEngine;
		
		// Render item texture
		renderItem.renderItemIntoGUI(font, engine, itemStack, 0,0);
		renderItem.renderItemOverlayIntoGUI(font, engine, itemStack, 0, 0, (String)null);
		
		if (Dota2Item.isSampleItemStack(itemStack)) {
			// Add the Secret shop icon
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			engine.bindTexture("/mods/" + Dota2Items.ID + "/textures/gui/secret_shop_item.png");
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(ICON_SIZE, 16, 0, 1, 1);
			tessellator.addVertexWithUV(ICON_SIZE, 16 - ICON_SIZE, 0, 1, 0);
			tessellator.addVertexWithUV(0, 16 - ICON_SIZE, 0, 0, 0);
			tessellator.addVertexWithUV(0, 16, 0, 0, 1);
			tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
