package hunternif.mc.dota2items.client.gui;

import hunternif.mc.dota2items.ClientProxy;
import hunternif.mc.dota2items.Dota2Items;
import hunternif.mc.dota2items.core.EntityStats;
import hunternif.mc.dota2items.core.StatsTracker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeSubscribe;

public class GuiHealthAndMana {
	private static final int HP_BAR_WIDTH = 81;
	
	private Minecraft mc;
	private FontRendererContourShadow fontRenderer;
	
	public GuiHealthAndMana(Minecraft mc) {
		this.mc = mc;
		this.fontRenderer = ClientProxy.fontRContourShadow;
	}
	
	@ForgeSubscribe
	public void onRenderHotBar(RenderGameOverlayEvent event) {
		// Only interested in Post-HotBar events ((almost) the end of overlay rendering)
		if (event.isCancelable() || event.type != ElementType.HOTBAR || mc.thePlayer.capabilities.isCreativeMode) {
			return;
		}
		int width = event.resolution.getScaledWidth();
		int height = event.resolution.getScaledHeight();
		
		//TODO make text positioning configurable; add an option to show Dota-like HP and mana bars.
		//NOTE I could make a sheet for smaller font size, if it's only 0123456789/+
		
		EntityStats stats = Dota2Items.stats.getOrCreateEntityStats(mc.thePlayer);
		int left = width / 2 - 91;
		int top = height - 38;
		String hp = stats.getHealth(mc.thePlayer) + "/" + stats.getMaxHealth();
		int strlen = fontRenderer.getStringWidth(hp);
		fontRenderer.drawStringWithShadow(hp, left - strlen - 1, top, 0xFF1313);
		if (StatsTracker.shouldHeal(mc.thePlayer, stats)) {
			String hpRegen = String.format("+%.2f", stats.getHealthRegen());
			fontRenderer.drawStringWithShadow(hpRegen, left + HP_BAR_WIDTH + 1, top, 0xFF6C6C);
		}
		
		int curMana = stats.getMana();
		int maxMana = stats.getMaxMana();
		if (maxMana > 0) {
			boolean renderArmor = ForgeHooks.getTotalArmorValue(mc.thePlayer) > 0;
			top = GuiManaBar.yPos + 1;
			String mana = curMana + "/" + maxMana;
			strlen = fontRenderer.getStringWidth(mana);
			fontRenderer.drawStringWithShadow(mana, left - strlen - 1, top, 0x2162F8);
			if (curMana < maxMana) {
				String manaRegen = String.format("+%.2f", stats.getManaRegen());
				fontRenderer.drawStringWithShadow(manaRegen, left + HP_BAR_WIDTH + 1, top, 0x4893D4);
			}
		}
	}
}
