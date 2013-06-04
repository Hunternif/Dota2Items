package hunternif.mc.dota2items;

import hunternif.mc.dota2items.core.ServerTickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class Dota2ItemsCommonProxy {
    // Client stuff
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }
    
    public void registerSounds() {}
    
    public void registerTickHandlers() {
    	TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
    }
}