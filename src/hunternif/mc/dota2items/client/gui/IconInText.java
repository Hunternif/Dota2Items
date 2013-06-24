package hunternif.mc.dota2items.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;

@SideOnly(Side.CLIENT)
public class IconInText implements Icon {
	public String texture;
	public int width;
	public int height;
	public int xOffset;
	public int yOffset;
	public String key;
	public int kerning;
	
	public IconInText(String key, int width, int height, String texture, int xOffset, int yOffset, int kerning) {
		this.key = key;
		this.texture = texture;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.kerning = kerning;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getOriginX() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getOriginY() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinU() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxU() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedU(double d0) {
		return (float)d0/16f;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinV() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxV() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedV(double d0) {
		return (float)d0/16f;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getIconName() {
		return key;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSheetWidth() {
		return width;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSheetHeight() {
		return height;
	}

}
