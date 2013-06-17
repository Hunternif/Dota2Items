package hunternif.mc.dota2items;

import hunternif.mc.dota2items.core.ClientTickHandler;
import hunternif.mc.dota2items.core.ServerTickHandler;
import hunternif.mc.dota2items.entity.EntityDota2Shopkeeper;
import hunternif.mc.dota2items.item.CooldownItem;
import hunternif.mc.dota2items.render.CooldownItemRenderer;
import hunternif.mc.dota2items.render.RenderDota2Shopkeeper;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	public static CooldownItemRenderer cooldownItemRenderer = new CooldownItemRenderer();
	
    @Override
    public void registerRenderers() {
    	MinecraftForge.EVENT_BUS.register(cooldownItemRenderer);
    	
    	for (Item item : Dota2Items.itemList) {
    		if (item instanceof CooldownItem) {
    			MinecraftForgeClient.registerItemRenderer(item.itemID, cooldownItemRenderer);
    		}
    	}
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityDota2Shopkeeper.class, new RenderDota2Shopkeeper());
    }
    
    @Override
    public void registerSounds() {
    	MinecraftForge.EVENT_BUS.register(new Dota2ItemSounds());
    }
    
    @Override
    public void registerTickHandlers() {
    	TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
    	TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
    }
}