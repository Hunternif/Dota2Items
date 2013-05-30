package hunternif.mc.dota2items.util;

import org.lwjgl.opengl.GL11;

public class GLUtil {
	public static void awesomePassingGlareEffect(float y) {
		//float y = 16F - 16F * (animationsToGo.floatValue() - 1F) / ((float)postCooldownEffectLength - 1F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		// Use 0 for clear stencil, enable stencil test
		GL11.glClearStencil(0);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		
		// Clear stencil buffer
	    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		
		// All drawing commands fail the stencil test, and are not
		// drawn, but increment the value in the stencil buffer.
		GL11.glStencilFunc(GL11.GL_NEVER, 0x0, 0x0);
		GL11.glStencilOp(GL11.GL_INCR, GL11.GL_INCR, GL11.GL_INCR);
		
		// Tilted linear glare mask
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(0, y, 0);
		GL11.glVertex3f(0, 1.5f*y + 2, 0);
		GL11.glVertex3f(16, 1.5f*y, 0);
		GL11.glVertex3f(16, y - 2, 0);
		GL11.glEnd();
		
		// Now, allow drawing, only where the stencil pattern is 0x1
	    // and do not make any further changes to the stencil buffer
		GL11.glStencilFunc(GL11.GL_EQUAL, 0x1, 0x1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(false);
		
		// Gradient square
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_COLOR);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(1, 1, 1, 0.5F);
		GL11.glVertex3f(16, 0, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glColor4f(1, 1, 1, 0);
		GL11.glVertex3f(0, 16, 0);
		GL11.glVertex3f(16, 16, 0);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
