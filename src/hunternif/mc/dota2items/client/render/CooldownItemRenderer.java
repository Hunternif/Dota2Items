package hunternif.mc.dota2items.client.render;

import hunternif.mc.dota2items.client.event.CooldownEndDisplayEvent;
import hunternif.mc.dota2items.core.ClientTickHandler;
import hunternif.mc.dota2items.item.CooldownItem;
import hunternif.mc.dota2items.util.MathUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CooldownItemRenderer implements IItemRenderer {
	/** Length of post-cooldown animation in ticks. */
	private static int postCooldownEffectLength = 10;
	
	/** See CooldownItem.inventoryStacks */
	private ItemStack[] inventoryStacks = new ItemStack[36];
	private Map<ItemStack, Integer> animationsToGoOffCooldown = new ConcurrentHashMap<ItemStack, Integer>();
	
	private static RenderItem renderItem = new RenderItem();
	
	
	private class ItemStackWrapper {
		public final ItemStack itemStack;
		public int ticks;
		public ItemStackWrapper(ItemStack itemStack) {
			this.itemStack = itemStack;
			this.ticks = postCooldownEffectLength;
		}
	}

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY && itemStack.getItem() instanceof CooldownItem;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		
		// Render item texture
		Icon icon = itemStack.getIconIndex();
		renderItem.renderIcon(0, 0, icon, 16, 16);
		
		// handleRenderType should ensure that the item is an AbstractCooldownItem
		CooldownItem item = (CooldownItem) itemStack.getItem();
		if (!item.isOnCooldown(itemStack)) {
			Integer animationsToGo = animationsToGoOffCooldown.get(itemStack);
			if (animationsToGo != null) {
				int elapsedTicks = ClientTickHandler.timer.elapsedTicks;
				// Cooldown stop effect
				
				// Effect from the firework particle
				GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture("/particles.png");
                
                int particleTextureIndex = 159 + (animationsToGo+1)/2;
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA(255, 255, 220, 255);
				double ul = (particleTextureIndex % 16) / 16.0F;
				double ur = ul + 0.0624375F;
				double vt = (particleTextureIndex / 16) / 16.0F;
				double vb = vt + 0.0624375F;
				tessellator.addVertexWithUV(12, 12, 0, ur, vb);
				tessellator.addVertexWithUV(12, 3, 0, ur, vt);
				tessellator.addVertexWithUV(3, 3, 0, ul, vt);
				tessellator.addVertexWithUV(3, 12, 0, ul, vb);
				tessellator.draw();
				
				GL11.glDisable(GL11.GL_BLEND);
				
				animationsToGo -= elapsedTicks;
				if (animationsToGo > 0)
					animationsToGoOffCooldown.put(itemStack, animationsToGo);
				else
					animationsToGoOffCooldown.remove(itemStack);
			}
			return;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
		tessellator.setColorRGBA(0, 0, 0, 128);
		
		float angle = item.getRemainingCooldown(itemStack) / item.getCooldown(itemStack) * MathUtil._2_PI;
		tessellator.addVertex(8D, 8D, 0.0D); // Center
		tessellator.addVertex(8D, 0D, 0.0D); // Top point
		// Top left corner
		if (angle >= MathUtil.PI_1_4) tessellator.addVertex(0D, 0D, 0.0D);
		// Bottom left corner
		if (angle >= MathUtil.PI_3_4) tessellator.addVertex(0D, 16D, 0.0D);
		// Bottom right corner
		if (angle >= MathUtil.PI_5_4) tessellator.addVertex(16D, 16D, 0.0D);
		// Top right MathUtil.corner
		if (angle >= MathUtil.PI_7_4) tessellator.addVertex(16D, 0D, 0.0D);
		// From this point on, calculate current hand position
		if (angle > MathUtil.PI_7_4)
			tessellator.addVertex(8D*(1F - MathUtil.tan(angle)), 0D, 0.0D);
		else if (angle > MathUtil.PI_5_4)
			tessellator.addVertex(16D, 8D*(1F + MathUtil.cot(angle)), 0.0D);
		else if (angle > MathUtil.PI_3_4)
			tessellator.addVertex(8D*(1F + MathUtil.tan(angle)), 16D, 0.0D);
		else if (angle > MathUtil.PI_1_4)
			tessellator.addVertex(0D, 8D*(1F - MathUtil.cot(angle)), 0.0D);
		else
			tessellator.addVertex(8D*(1F - MathUtil.tan(angle)), 0D, 0.0D);
		
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Add remaining cooldown text
		String text = String.valueOf(MathHelper.ceiling_float_int(item.getRemainingCooldown(itemStack)));
		int color = 0xFFFFFF;
		int x = (16 - fontRenderer.getStringWidth(text)) / 2;
		int y = (16 - fontRenderer.FONT_HEIGHT) / 2 + 1;
		fontRenderer.drawStringWithShadow(text, x, y, color);
	}
	
	@ForgeSubscribe
	public void onCooldownEnd(CooldownEndDisplayEvent event) {
		ItemStack prevStackInThatSlot = inventoryStacks[event.slotId];
		if (prevStackInThatSlot != null) {
			// New item stack reference arrived, it should replace the previous
			// one to prevent memory leaks.
			animationsToGoOffCooldown.remove(prevStackInThatSlot);
		}
		inventoryStacks[event.slotId] = event.itemStack;
		animationsToGoOffCooldown.put(event.itemStack, postCooldownEffectLength);
	}
}
