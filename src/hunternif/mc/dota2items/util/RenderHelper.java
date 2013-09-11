package hunternif.mc.dota2items.util;


import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHelper {
	public static RenderItem itemRenderer = new RenderItem();
	
	public static void drawHoveringText(List<String> par1List, int par2, int par3, FontRenderer font) {
		if (!par1List.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			//net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int windowWidth = scaledresolution.getScaledWidth();
			int windowHeight = scaledresolution.getScaledHeight();
			
			int k = 0;
			Iterator iterator = par1List.iterator();

			while (iterator.hasNext()) {
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k) {
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1) {
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > windowWidth) {
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > windowHeight) {
				j1 = windowHeight - k1 - 6;
			}

			//zLevel = 300.0F;
			//itemRenderer.zLevel = 300.0F;
			int l1 = -267386864;
			drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < par1List.size(); ++k2) {
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0) {
					j1 += 2;
				}

				j1 += 10;
			}

			//zLevel = 0.0F;
			//itemRenderer.zLevel = 0.0F;
			//GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			//net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
	
	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
		float f = (float)(par5 >> 24 & 255) / 255.0F;
		float f1 = (float)(par5 >> 16 & 255) / 255.0F;
		float f2 = (float)(par5 >> 8 & 255) / 255.0F;
		float f3 = (float)(par5 & 255) / 255.0F;
		float f4 = (float)(par6 >> 24 & 255) / 255.0F;
		float f5 = (float)(par6 >> 16 & 255) / 255.0F;
		float f6 = (float)(par6 >> 8 & 255) / 255.0F;
		float f7 = (float)(par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		float zLevel = 300;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double)par3, (double)par2, (double)zLevel);
		tessellator.addVertex((double)par1, (double)par2, (double)zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double)par1, (double)par4, (double)zLevel);
		tessellator.addVertex((double)par3, (double)par4, (double)zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void drawTextureRect(ResourceLocation texture, int x, int y, int width, int height) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.func_110577_a(texture);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + width, y + height, 0, 1, 1);
		tessellator.addVertexWithUV(x + width, y, 0, 1, 0);
		tessellator.addVertexWithUV(x, y, 0, 0, 0);
		tessellator.addVertexWithUV(x, y + height, 0, 0, 1);
		tessellator.draw();
		//GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	/**
	 * @param angle in radians
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param alpha (color is black)
	 */
	public static void drawShadowClock(float angle, int x, int y, int width, int height, float alpha) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
		tessellator.setColorRGBA_F(0, 0, 0, alpha);
		
		tessellator.addVertex(x + width/2, y + height/2, 0); // Center
		tessellator.addVertex(x + width/2, y, 0); // Top point
		// Top left corner
		if (angle >= MathUtil.PI_1_4) tessellator.addVertex(x, y, 0);
		// Bottom left corner
		if (angle >= MathUtil.PI_3_4) tessellator.addVertex(x, y + height, 0);
		// Bottom right corner
		if (angle >= MathUtil.PI_5_4) tessellator.addVertex(x + width, y + height, 0);
		// Top right corner
		if (angle >= MathUtil.PI_7_4) tessellator.addVertex(x + width, y, 0);
		// From this point on, calculate current hand position
		if (angle > MathUtil.PI_7_4)
			tessellator.addVertex(x + width/2*(1F - MathUtil.tan(angle)), y, 0);
		else if (angle > MathUtil.PI_5_4)
			tessellator.addVertex(x + width, y + height/2*(1F + MathUtil.cot(angle)), 0);
		else if (angle > MathUtil.PI_3_4)
			tessellator.addVertex(x + width/2*(1F + MathUtil.tan(angle)), y + height, 0);
		else if (angle > MathUtil.PI_1_4)
			tessellator.addVertex(x, y + height/2*(1F - MathUtil.cot(angle)), 0);
		else
			tessellator.addVertex(x + width/2*(1F - MathUtil.tan(angle)), y, 0);
		
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
