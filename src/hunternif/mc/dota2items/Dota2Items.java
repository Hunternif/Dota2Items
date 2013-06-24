package hunternif.mc.dota2items;

import hunternif.mc.dota2items.client.gui.GuiHandler;
import hunternif.mc.dota2items.client.network.ClientPacketHandler;
import hunternif.mc.dota2items.core.Dota2PlayerTracker;
import hunternif.mc.dota2items.core.Mechanics;
import hunternif.mc.dota2items.entity.EntityShopkeeper;
import hunternif.mc.dota2items.entity.ShopkeeperSpawner;
import hunternif.mc.dota2items.inventory.Dota2CreativeTab;
import hunternif.mc.dota2items.inventory.InventoryShop;
import hunternif.mc.dota2items.network.ServerPacketHandler;
import hunternif.mc.dota2items.tileentity.TileEntityCyclone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=Dota2Items.ID, name=Dota2Items.NAME, version=Dota2Items.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true,
	clientPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={Dota2Items.CHANNEL}, packetHandler=ClientPacketHandler.class),
	serverPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={Dota2Items.CHANNEL}, packetHandler=ServerPacketHandler.class))
public class Dota2Items {
	public static final String ID = "Dota2Items";
	public static final String NAME = "Dota 2 Items";
	public static final String VERSION = "0.4";
	public static final String CHANNEL = ID;
	
	public static CreativeTabs dota2CreativeTab;
	public static List<Item> itemList = new ArrayList<Item>();
	
	public static final Dota2PlayerTracker playerTracker = new Dota2PlayerTracker();
	public static final Mechanics mechanics = new Mechanics();
	public static final ShopkeeperSpawner shopkeeperSpawner = new ShopkeeperSpawner();
	
	@Instance(ID)
	public static Dota2Items instance;
	
	@SidedProxy(clientSide="hunternif.mc.dota2items.ClientProxy", serverSide="hunternif.mc.dota2items.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.registerSounds();
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		Config.preLoad(config);
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		dota2CreativeTab = new Dota2CreativeTab("dota2ItemTab");
		LanguageRegistry.instance().addStringLocalization("itemGroup.dota2ItemTab", "en_US", "Dota 2 Items");
		
		Config.load();
		
		InventoryShop.populate();
		
		GameRegistry.registerTileEntity(TileEntityCyclone.class, "Cyclone");
		
		EntityRegistry.registerModEntity(EntityShopkeeper.class, "Dota2Shopkeeper", EntityRegistry.findGlobalUniqueEntityId(), instance, 80, 3, true);
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		proxy.registerRenderers();
		proxy.registerTickHandlers();
		MinecraftForge.EVENT_BUS.register(mechanics);
		GameRegistry.registerPlayerTracker(playerTracker);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}