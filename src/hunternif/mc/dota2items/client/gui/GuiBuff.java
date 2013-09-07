package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.buff.BuffInstance;
import hunternif.mc.dota2items.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiBuff {
	private static final ResourceLocation buffBackground = new ResourceLocation(Dota2Items.ID+":textures/gui/buff_bg.png");
	private static final ResourceLocation buffFrame = new ResourceLocation(Dota2Items.ID+":textures/gui/buff_frame.png");
	private static final ResourceLocation debuffFrame = new ResourceLocation(Dota2Items.ID+":textures/gui/debuff_frame.png");
	
	public static final int BUFF_FRAME_SIZE = 20;
	private static final int BUFF_FRAME = (BUFF_FRAME_SIZE - 16)/2;
	
	public BuffInstance buffInst;
	public int x;
	public int y;
	private Minecraft mc;
	
	public GuiBuff(BuffInstance buff, int x, int y) {
		mc = Minecraft.getMinecraft();
		this.buffInst = buff;
		this.x = x;
		this.y = y;
	}
	
	public void render() {
		// Draw frame with timer:
		RenderHelper.drawTextureRect(buffInst.isFriendly ? buffFrame : debuffFrame, x, y, BUFF_FRAME_SIZE, BUFF_FRAME_SIZE);
		// Draw timer shade, if the buff is not permanent:
		if (!buffInst.isPermanent()) {
			float elapsed = mc.theWorld.getTotalWorldTime() - buffInst.startTime;
			float duration = buffInst.getDuration();
			float angle = elapsed / duration * MathUtil._2_PI;
			RenderHelper.drawShadowClock(angle, x, y, BUFF_FRAME_SIZE, BUFF_FRAME_SIZE, 0.5f);
		}
		// Draw background:
		RenderHelper.drawTextureRect(buffBackground, x + BUFF_FRAME, y + BUFF_FRAME, 16, 16);
		// Draw buff icon, if any:
		if (buffInst.buff.iconTexture != null) {
			RenderHelper.drawTextureRect(buffInst.buff.iconTexture, x + BUFF_FRAME, y + BUFF_FRAME, 16, 16);
		}
	}
	
	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX < x + BUFF_FRAME_SIZE && mouseY < y + BUFF_FRAME_SIZE;
	}
}
