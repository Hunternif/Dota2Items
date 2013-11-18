package hunternif.mc.dota2items;

import hunternif.mc.dota2items.client.RenderTickHandler;
import hunternif.mc.dota2items.client.SwingTickHandler;
import hunternif.mc.dota2items.client.gui.FontRendererContourShadow;
import hunternif.mc.dota2items.client.gui.FontRendererWithIcons;
import hunternif.mc.dota2items.client.gui.GuiDotaStats;
import hunternif.mc.dota2items.client.gui.GuiGold;
import hunternif.mc.dota2items.client.gui.GuiHealthAndMana;
import hunternif.mc.dota2items.client.gui.GuiManaBar;
import hunternif.mc.dota2items.client.gui.IconInText;
import hunternif.mc.dota2items.client.render.CooldownItemRenderer;
import hunternif.mc.dota2items.client.render.DummyRenderer;
import hunternif.mc.dota2items.client.render.RenderClarityEffect;
import hunternif.mc.dota2items.client.render.RenderDagonBolt;
import hunternif.mc.dota2items.client.render.RenderMidasEffect;
import hunternif.mc.dota2items.client.render.RenderShopkeeper;
import hunternif.mc.dota2items.config.CfgInfo;
import hunternif.mc.dota2items.config.Config;
import hunternif.mc.dota2items.config.DescriptionBuilder;
import hunternif.mc.dota2items.effect.EffectClarity;
import hunternif.mc.dota2items.effect.EntityDagonBolt;
import hunternif.mc.dota2items.effect.EntityMidasEffect;
import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.entity.EntityWrapper;
import hunternif.mc.dota2items.item.ActiveItem;
import hunternif.mc.dota2items.item.Dota2Item;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	public static final CooldownItemRenderer cooldownItemRenderer = new CooldownItemRenderer();
	
	public static final ReloadableResourceManager resourceManager = new SimpleReloadableResourceManager(new MetadataSerializer());
	
	public static final FontRendererWithIcons fontRWithIcons = new FontRendererWithIcons(
			Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
	
	public static final FontRendererContourShadow fontRContourShadow = new FontRendererContourShadow(
			Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
	
	public static final IconInText ICON_GOLD = new IconInText("$gold$", 12, 12, Dota2Items.ID+":textures/gui/gold_coins.png", -1, -3, 2);
	public static final IconInText ICON_COOLDOWN = new IconInText("$cd$", 7, 7, Dota2Items.ID+":textures/gui/cooldown.png", 0, 0, 3);
	public static final IconInText ICON_MANACOST = new IconInText("$manacost$", 7, 7, Dota2Items.ID+":textures/gui/manacost.png", 0, 0, 3);
	
	private final RenderTickHandler hudTickHandler = new RenderTickHandler();
	private final SwingTickHandler swingTickHandler = new SwingTickHandler();
	
	@Override
	public void registerRenderers() {
		// Build item description
		try {
			Field[] fields = Config.class.getFields();
			for (Field field : fields) {
				if (field.getType().equals(CfgInfo.class)) {
					CfgInfo<?> info = (CfgInfo)field.get(null);
					if (info.instance instanceof Dota2Item) {
						DescriptionBuilder.build((CfgInfo<? extends Dota2Item>)info);
					}
				}
			}
		} catch (Exception e) {
			Dota2Items.logger.warning("Failed to build item description: " + e.toString());
		}
		
		resourceManager.registerReloadListener(fontRWithIcons);
		resourceManager.registerReloadListener(fontRContourShadow);
		fontRWithIcons.registerIcon(ICON_GOLD);
		fontRWithIcons.registerIcon(ICON_COOLDOWN);
		fontRWithIcons.registerIcon(ICON_MANACOST);
		
		Minecraft mc = Minecraft.getMinecraft();
		MinecraftForge.EVENT_BUS.register(cooldownItemRenderer);
		MinecraftForge.EVENT_BUS.register(new GuiManaBar(mc));
		MinecraftForge.EVENT_BUS.register(new GuiHealthAndMana(mc));
		hudTickHandler.registerHUD(new GuiGold(mc));
		hudTickHandler.registerHUD(new GuiDotaStats(mc));
		
		for (Item item : Dota2Items.itemList) {
			if (item instanceof ActiveItem) {
				MinecraftForgeClient.registerItemRenderer(item.itemID, cooldownItemRenderer);
			}
		}
		
		RenderingRegistry.registerEntityRenderingHandler(EntityShopkeeper.class, new RenderShopkeeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityDagonBolt.class, new RenderDagonBolt());
		RenderingRegistry.registerEntityRenderingHandler(EntityWrapper.class, new DummyRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EffectClarity.class, new RenderClarityEffect());
		RenderingRegistry.registerEntityRenderingHandler(EntityMidasEffect.class, new RenderMidasEffect());
	}
	
	@Override
	public void registerSounds() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void registerTickHandlers() {
		super.registerTickHandlers();
		TickRegistry.registerTickHandler(hudTickHandler, Side.CLIENT);
		TickRegistry.registerTickHandler(swingTickHandler, Side.CLIENT);
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
        try {
        	for (Sound sound : Sound.values()) {
        		if (!sound.isRandom()) {
        			event.manager.soundPoolSounds.addSound(sound.getName()+".ogg");
        		} else {
        			for (int i = 1; i <= sound.randomVariants; i++) {
        				event.manager.soundPoolSounds.addSound(sound.getName()+i+".ogg");
        			}
        		}
        	}
        }
        catch (Exception e) {
        	Dota2Items.logger.warning("Failed to register one or more sounds.");
        }
    }
}