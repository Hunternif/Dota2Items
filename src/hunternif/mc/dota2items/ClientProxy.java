package hunternif.mc.dota2items;

import hunternif.mc.dota2items.core.ClientTickHandler;
import hunternif.mc.dota2items.core.ServerTickHandler;
import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.gui.FontRendererWithIcons;
import hunternif.mc.dota2items.gui.GuiGold;
import hunternif.mc.dota2items.gui.IconInText;
import hunternif.mc.dota2items.item.CooldownItem;
import hunternif.mc.dota2items.render.CooldownItemRenderer;
import hunternif.mc.dota2items.render.RenderShopkeeper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	public static final CooldownItemRenderer cooldownItemRenderer = new CooldownItemRenderer();
	public static final FontRendererWithIcons fontRenderer = new FontRendererWithIcons(
			Minecraft.getMinecraft().gameSettings, "/font/default.png", Minecraft.getMinecraft().renderEngine, false);
	public static IconInText ICON_GOLD = new IconInText("$gold$", 12, 12, "/mods/"+Dota2Items.ID+"/textures/gui/gold_coins.png", -1, -3, 3);
	public static IconInText ICON_COOLDOWN = new IconInText("$cd$", 8, 8, "/mods/"+Dota2Items.ID+"/textures/gui/cooldown.png", 0, 0, 4);
	{
		fontRenderer.registerIcon(ICON_GOLD);
		fontRenderer.registerIcon(ICON_COOLDOWN);
	}
	public static GuiGold guiGold = new GuiGold();
	
	@Override
	public void registerRenderers() {
		MinecraftForge.EVENT_BUS.register(cooldownItemRenderer);
		
		for (Item item : Dota2Items.itemList) {
			if (item instanceof CooldownItem) {
				MinecraftForgeClient.registerItemRenderer(item.itemID, cooldownItemRenderer);
			}
		}
		
		RenderingRegistry.registerEntityRenderingHandler(EntityShopkeeper.class, new RenderShopkeeper());
	}
	
	@Override
	public void registerSounds() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void registerTickHandlers() {
		TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
        try {
        	for (Sound sound : Sound.values()) {
        		event.manager.soundPoolSounds.addSound(sound+".wav", Dota2Items.class.getResource("/mods/"+Dota2Items.ID+"/sounds/"+sound+".wav"));
        	}
        }
        catch (Exception e) {
            System.err.println("Failed to register one or more sounds.");
        }
    }
}