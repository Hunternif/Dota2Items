package hunternif.mc.dota2items;

import hunternif.mc.dota2items.item.CooldownItem;
import hunternif.mc.dota2items.render.CooldownItemRenderer;
import hunternif.mc.dota2items.util.RenderTimer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class Dota2ItemsClientProxy extends Dota2ItemsCommonProxy {
	
	//public static Canvas tempCanvas = new Canvas();
	
	public static CooldownItemRenderer cooldownItemRenderer = new CooldownItemRenderer();
	public static RenderTimer renderTimer = new RenderTimer(20);
	//public static TestPreProcessor testPreProcessor = new TestPreProcessor(tempCanvas);
	//public static TestPostProcessor testPostProcessor = new TestPostProcessor(tempCanvas);
	
    @Override
    public void registerRenderers() {
    	//renderTimer.registerPreProcessor(testPreProcessor);
    	//renderTimer.registerPostProcessor(testPostProcessor);
    	TickRegistry.registerTickHandler(renderTimer, Side.CLIENT);
    	
    	MinecraftForge.EVENT_BUS.register(cooldownItemRenderer);
    	
    	for (Item item : Dota2Items.itemList) {
    		if (item instanceof CooldownItem) {
    			MinecraftForgeClient.registerItemRenderer(item.itemID, cooldownItemRenderer);
    		}
    	}
    }
        
}