package hunternif.mc.dota2items.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

@Deprecated
public class TutorialDamagedItemRenderer implements IItemRenderer {
	private static RenderItem renderItem = new RenderItem();

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		
		// ====================== Render item texture ======================
		// Get icon index for the texture
		Icon icon = itemStack.getIconIndex();
		// Use vanilla code to render the icon in a 16x16 square of inventory slot
		renderItem.renderIcon(0, 0, icon, 16, 16);
		
		// ====================== Render OpenGL square shape ======================
		// Disable texturing, for now we only need colored shapes
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// Enable transparency (see second tutorial link above)
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// We don't call methods on GL11 directly, we use Minecraft's Tessellator instead
		//Tessellator tessellator = Tessellator.instance;
		// Set drawing mode (see first tutorial link above)
		//tessellator.startDrawing(GL11.GL_QUADS);
		GL11.glBegin(GL11.GL_QUADS);
		// Set semi-transparent black color
		//tessellator.setColorRGBA(0, 0, 0, 128);
		GL11.glColor4f(0F, 0F, 0F, 0.5F);
		
		// Draw a 8x8 square
		GL11.glVertex3d(0D, 0D, 0D);
		GL11.glVertex3d(0D, 8D, 0D);
		GL11.glVertex3d(8D, 8D, 0D);
		GL11.glVertex3d(8D, 0D, 0D);
		//tessellator.addVertex(0D, 0D, 0.0D);
		//tessellator.addVertex(0D, 8D, 0.0D);
		//tessellator.addVertex(8D, 8D, 0.0D);
		//tessellator.addVertex(8D, 0D, 0.0D);
		
		//tessellator.draw();
		GL11.glEnd();
		
		// Turn off unneeded transparency flags
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		
		// ====================== Render text ======================
		// Enable texturing, because Minecraft text font is actually a texture.
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// Get our text value
		String text = Integer.toString(itemStack.getItemDamage());
		// Draw our text at (1, 1) with white color
		fontRenderer.drawStringWithShadow(text, 1, 1, 0xFFFFFF);
	}
}
