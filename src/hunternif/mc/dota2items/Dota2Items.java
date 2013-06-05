package hunternif.mc.dota2items;

import hunternif.mc.dota2items.block.BlockCycloneContainer;
import hunternif.mc.dota2items.core.Mechanics;
import hunternif.mc.dota2items.core.inventory.Dota2ItemCreativeTab;
import hunternif.mc.dota2items.item.BlinkDagger;
import hunternif.mc.dota2items.item.Dota2Item;
import hunternif.mc.dota2items.item.Dota2Logo;
import hunternif.mc.dota2items.item.EulsScepter;
import hunternif.mc.dota2items.item.QuellingBlade;
import hunternif.mc.dota2items.item.Tango;
import hunternif.mc.dota2items.network.ClientPacketHandler;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=Dota2Items.ID, name=Dota2Items.NAME, version=Dota2Items.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={Dota2Items.CHANNEL},
	clientPacketHandlerSpec=@NetworkMod.SidedPacketHandler(channels={Dota2Items.CHANNEL}, packetHandler=ClientPacketHandler.class))
public class Dota2Items {
	public static final String ID = "Dota2Items";
	public static final String NAME = "Dota 2 Items";
	public static final String VERSION = "0.4";
	public static final String CHANNEL = ID;
	
	public static CreativeTabs dota2CreativeTab;
	public static List<Item> itemList = new ArrayList<Item>();
	private static int dota2LogoId;
	public static Item dota2Logo;
	private static int blinkDaggerId;
	public static Dota2Item blinkDagger;
	private static int tangoId;
	public static Dota2Item tango;
	private static int quellingBladeId;
	public static Dota2Item quellingBlade;
	private static int eulId;
	public static Dota2Item eulsScepter;
	
	private static int cycloneContainerId;
	public static BlockCycloneContainer cycloneContainer;
	
	public static Mechanics mechanics = new Mechanics();
	
	@Instance(ID)
	public static Dota2Items instance;
	
	@SidedProxy(clientSide="hunternif.mc.dota2items.ClientProxy", serverSide="hunternif.mc.dota2items.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.registerSounds();
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		dota2LogoId = config.getItem(Dota2Logo.NAME, 27000).getInt();
		blinkDaggerId = config.getItem(BlinkDagger.NAME, 27001).getInt();
		tangoId = config.getItem(Tango.NAME, 27002).getInt();
		quellingBladeId = config.getItem(QuellingBlade.NAME, 27003).getInt();
		eulId = config.getItem(EulsScepter.NAME, 27004).getInt();
		cycloneContainerId = config.getBlock(BlockCycloneContainer.NAME, 2700).getInt();
		config.save();
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		dota2CreativeTab = new Dota2ItemCreativeTab("dota2ItemTab");
		LanguageRegistry.instance().addStringLocalization("itemGroup.dota2ItemTab", "en_US", "Dota 2 Items");
		
		dota2Logo = new Dota2Logo(dota2LogoId);
		LanguageRegistry.addName(dota2Logo, "Dota 2 Logo");
		itemList.add(dota2Logo);
		
		blinkDagger = new BlinkDagger(blinkDaggerId);
		LanguageRegistry.addName(blinkDagger, "Blink Dagger");
		itemList.add(blinkDagger);
		
		tango = new Tango(tangoId);
		LanguageRegistry.addName(tango, "Tango");
		itemList.add(tango);
		
		quellingBlade = new QuellingBlade(quellingBladeId);
		LanguageRegistry.addName(quellingBlade, "Quelling Blade");
		itemList.add(quellingBlade);
		
		eulsScepter = new EulsScepter(eulId);
		LanguageRegistry.addName(eulsScepter, "Eul's Scepter of Divinity");
		itemList.add(eulsScepter);
		
		cycloneContainer = new BlockCycloneContainer(cycloneContainerId);
		GameRegistry.registerBlock(cycloneContainer, BlockCycloneContainer.NAME);
		GameRegistry.registerTileEntity(TileEntityCyclone.class, TileEntityCyclone.NAME);
		
		proxy.registerRenderers();
		proxy.registerTickHandlers();
		MinecraftForge.EVENT_BUS.register(mechanics);
		GameRegistry.registerPlayerTracker(mechanics.playerTracker);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}