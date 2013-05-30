package hunternif.mc.dota2items;

import hunternif.mc.dota2items.block.BlockCycloneContainer;
import hunternif.mc.dota2items.item.BlinkDagger;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.Dota2Logo;
import hunternif.mc.dota2items.item.EulsScepter;
import hunternif.mc.dota2items.item.QuellingBlade;
import hunternif.mc.dota2items.item.Tango;
import hunternif.mc.dota2items.mechanics.Dota2ItemMechanics;
import hunternif.mc.dota2items.mechanics.Dota2Timer;
import hunternif.mc.dota2items.mechanics.inventory.Dota2ItemTab;
import hunternif.mc.dota2items.mechanics.motion.Dota2EntityMotionHandler;
import hunternif.mc.dota2items.network.Dota2ItemsPacketHandler;
import hunternif.mc.dota2items.tileentity.TileEntityCyclone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=Dota2Items.ID, name=Dota2Items.NAME, version=Dota2Items.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={Dota2Items.CHANNEL}, packetHandler=Dota2ItemsPacketHandler.class)
public class Dota2Items {
	public static final String ID = "Dota2Items";
	public static final String NAME = "Dota 2 Items";
	public static final String VERSION = "0.4";
	public static final String CHANNEL = ID;
	
	public static CreativeTabs dota2CreativeTab;
	public static List<Item> itemList = new ArrayList<Item>();
	//public static Item particlesDummyItem;
	public static Item dota2Logo;
	public static Dota2Item blinkDagger;
	public static Dota2Item tango;
	public static Dota2Item quellingBlade;
	public static Dota2Item eulsScepter;
	
	public static BlockCycloneContainer cycloneContainer;
	
	//TODO automate release packaging
	public static Configuration config;
	
	public static Dota2Timer timer = new Dota2Timer();
	public static Dota2ItemMechanics mechanics = new Dota2ItemMechanics();
	public static Dota2ItemSounds sounds = new Dota2ItemSounds();
	public static Dota2EntityMotionHandler immobilizer = new Dota2EntityMotionHandler();
	
	@Instance(ID)
	public static Dota2Items instance;
	
	@SidedProxy(clientSide="hunternif.mc.dota2items.Dota2ItemsClientProxy", serverSide="hunternif.mc.dota2items.Dota2ItemsCommonProxy")
	public static Dota2ItemsCommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Side side =  FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT)
			MinecraftForge.EVENT_BUS.register(sounds);
		config = new Configuration(event.getSuggestedConfigurationFile());
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		dota2CreativeTab = new Dota2ItemTab("dota2ItemTab");
		LanguageRegistry.instance().addStringLocalization("itemGroup.dota2ItemTab", "en_US", "Dota 2 Items");
		
		int dota2LogoId = config.getItem(Dota2Logo.NAME, 27000).getInt();
		dota2Logo = new Dota2Logo(dota2LogoId);
		LanguageRegistry.addName(dota2Logo, "Dota 2 Logo");
		itemList.add(dota2Logo);
		
		int blinkDaggerId = config.getItem(BlinkDagger.NAME, 27001).getInt();
		blinkDagger = new BlinkDagger(blinkDaggerId);
		LanguageRegistry.addName(blinkDagger, "Blink Dagger");
		itemList.add(blinkDagger);
		
		int tangoId = config.getItem(Tango.NAME, 27002).getInt();
		tango = new Tango(tangoId);
		LanguageRegistry.addName(tango, "Tango");
		itemList.add(tango);
		
		int quellingBladeId = config.getItem(QuellingBlade.NAME, 27003).getInt();
		quellingBlade = new QuellingBlade(quellingBladeId);
		LanguageRegistry.addName(quellingBlade, "Quelling Blade");
		itemList.add(quellingBlade);
		
		int eulId = config.getItem(EulsScepter.NAME, 27004).getInt();
		eulsScepter = new EulsScepter(eulId);
		LanguageRegistry.addName(eulsScepter, "Eul's Scepter of Divinity");
		itemList.add(eulsScepter);
		
		// This is not a good practice
		/*int particlesDummyId = config.getItem(Dota2ParticlesDummyItem.NAME, 27005).getInt();
		particlesDummyItem = new Dota2ParticlesDummyItem(particlesDummyId);
		LanguageRegistry.addName(particlesDummyItem, "dummy item for particles");
		itemList.add(particlesDummyItem);*/
		
		int cycloneContainerId = config.getBlock(BlockCycloneContainer.NAME, 2700).getInt();
		cycloneContainer = new BlockCycloneContainer(cycloneContainerId);
		GameRegistry.registerBlock(cycloneContainer, BlockCycloneContainer.NAME);
		GameRegistry.registerTileEntity(TileEntityCyclone.class, TileEntityCyclone.NAME);
		
		proxy.registerRenderers();
		MinecraftForge.EVENT_BUS.register(mechanics);
		MinecraftForge.EVENT_BUS.register(immobilizer);
		GameRegistry.registerPlayerTracker(mechanics.playerTracker);
		
		TickRegistry.registerTickHandler(timer, Side.CLIENT);
		TickRegistry.registerTickHandler(timer, Side.SERVER);
		timer.registerHandler(immobilizer);
		timer.registerHandler(mechanics);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}