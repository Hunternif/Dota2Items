package hunternif.mc.dota2items.render;

import java.awt.Canvas;
import java.awt.Graphics;

import net.minecraft.client.Minecraft;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class TestPostProcessor implements ISimpleRenderer {
	
	private Canvas tempCanvas;
	
	public TestPostProcessor(Canvas tempCanvas) {
		this.tempCanvas = tempCanvas;
	}
	
	@Override
	public void render() {
		//Minecraft.getMinecraft().displayWidth /= 3;
		
		try {
			Display.swapBuffers();
			//Display.setParent(Minecraft.getMinecraft().mcCanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//Minecraft.getMinecraft().displayWidth /= 3;
		//GL11.glScalef(1, 0.333333333333333333333333f, 1);
		//GL11.glColorMask(true, false, false, true);
	}

}
