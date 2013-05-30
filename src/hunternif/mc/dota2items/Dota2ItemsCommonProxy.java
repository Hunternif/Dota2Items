package hunternif.mc.dota2items;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class Dota2ItemsCommonProxy {
    // Client stuff
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    	//TickRegistry.registerTickHandler(Dota2Items.immobilizer, Side.SERVER);
    }
}