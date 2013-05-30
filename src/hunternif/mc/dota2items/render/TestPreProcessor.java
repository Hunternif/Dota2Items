package hunternif.mc.dota2items.render;

import java.awt.Canvas;

import net.minecraft.client.Minecraft;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class TestPreProcessor implements ISimpleRenderer {
	
	private Canvas tempCanvas;
	private DisplayMode displayMode;
	
	public TestPreProcessor(Canvas tempCanvas) {
		this.tempCanvas = tempCanvas;
	}
	
	@Override
	public void render() {
		//GL11.glDepthMask(true);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | 256 | 16640);
		
		//Minecraft.getMinecraft().displayHeight /= 3;
		//int width = Minecraft.getMinecraft().displayWidth;
		//Minecraft.getMinecraft().mcCanvas. getGraphics().translate(-width, 0);
		
		/*int width = Minecraft.getMinecraft().displayWidth*3;
		int height = Minecraft.getMinecraft().displayHeight;
		System.out.println(Display.getDisplayMode().getWidth());*/
		/*try {
			Display.swapBuffers();
			Minecraft.getMinecraft().displayWidth*=3;
			//if (displayMode == null || displayMode.getWidth() != width || displayMode.getHeight() != height) {
			//	displayMode = new DisplayMode(width, height);
			//	Display.setDisplayMode(displayMode);
			//}
			//Display.setParent(tempCanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}*/
		
		/*GL11.glPushMatrix();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(150, 150);
		GL11.glVertex2f(150, 100);
		GL11.glVertex2f(100, 100);
		GL11.glVertex2f(100, 150);
		GL11.glEnd();
		
		GL11.glPopMatrix();*/
	}

}
