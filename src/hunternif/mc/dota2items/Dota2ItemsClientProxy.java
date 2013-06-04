package hunternif.mc.dota2items;

import hunternif.mc.dota2items.core.ClientTickHandler;
import hunternif.mc.dota2items.core.ServerTickHandler;
import hunternif.mc.dota2items.item.CooldownItem;
import hunternif.mc.dota2items.render.CooldownItemRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class Dota2ItemsClientProxy extends Dota2ItemsCommonProxy {
	public static CooldownItemRenderer cooldownItemRenderer = new CooldownItemRenderer();
	
    @Override
    public void registerRenderers() {
    	MinecraftForge.EVENT_BUS.register(cooldownItemRenderer);
    	
    	for (Item item : Dota2Items.itemList) {
    		if (item instanceof CooldownItem) {
    			MinecraftForgeClient.registerItemRenderer(item.itemID, cooldownItemRenderer);
    		}
    	}
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