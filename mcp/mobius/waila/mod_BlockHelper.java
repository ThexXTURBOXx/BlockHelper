package mcp.mobius.waila;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import java.io.File;
import java.lang.reflect.Field;

import com.google.common.eventbus.EventBus;

import java.util.logging.Logger;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.client.ConfigKeyHandler;
import mcp.mobius.waila.network.WailaConnectionHandler;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.DecoratorRenderer;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.server.ProxyServer;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.relauncher.Side;
import mcp.mobius.waila.commands.CommandDumpHandlers;

@NetworkMod(channels = {mod_BlockHelper.CHANNEL}, connectionHandler = WailaConnectionHandler.class, packetHandler = WailaPacketHandler.class)
public class mod_BlockHelper extends BaseMod {

	public static final String PACKAGE = "mcp.mobius.waila.";
	public static final String MOD_ID = "mod_BlockHelper";
	public static final String NAME = "Block Helper";
	public static final String VERSION = "2.0.0-pre1";
	public static final String MC_VERSION = "1.5.2";
	public static final String CHANNEL = "BlockHelper";
	public static mod_BlockHelper INSTANCE;

	@SidedProxy(clientSide=PACKAGE+"client.ProxyClient", serverSide=PACKAGE+"server.ProxyServer")
	public static ProxyServer proxy;
	public static Logger log = Logger.getLogger(NAME);
	static {
		log.setParent(FMLLog.getLogger());
	}
	public boolean serverPresent = false;

	public static String getModId(){
		return MOD_ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public void load() {
		INSTANCE = this;

		new Thread(new BlockHelperUpdater(), "Block Helper Version Check").start();

		// PRE INIT
		Configuration cfg = new Configuration(new File((File) FMLInjectionData.data()[6], "config/BlockHelper.cfg"));
		ConfigHandler.instance().loadDefaultConfig(cfg);
		OverlayConfig.updateColors();
		MinecraftForge.EVENT_BUS.register(new DecoratorRenderer());
		MinecraftForge.EVENT_BUS.register(this);

		// INIT
		try {
			Field eBus = FMLModContainer.class.getDeclaredField("eventBus");
			eBus.setAccessible(true);
			EventBus FMLbus = (EventBus) eBus.get(FMLCommonHandler.instance().findContainerFor(this));
			FMLbus.register(this);
		} catch (Throwable t) {}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			ConfigKeyHandler.init(this);
			TickRegistry.registerTickHandler(WailaTickHandler.instance(), Side.CLIENT);
		}

		// POST INIT
		proxy.registerHandlers();
    	ModIdentification.init();

    	/*
        if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_KEYBIND, true)){

	        for (String key: ModIdentification.keyhandlerStrings.keySet()){
	        	String orig  = I18n.getString(key);
	        	if (orig.equals(key))
	        		orig = LanguageRegistry.instance().getStringLocalization(key);
	        	if (orig.equals(key))
	        		orig = LangUtil.translateG(key);
	        	if (orig.isEmpty())
	        		orig = key;

	        	String modif;
	        	if (orig.startsWith("[") || orig.contains(":"))
	        		modif = orig;
	        	else{
	        		String id = ModIdentification.keyhandlerStrings.get(key);

	        		if (id.contains("."))
	        			id = id.split("\\.")[0];

	        		if (id.length() > 10)
	        			id = id.substring(0, 11);

	        		if (id.isEmpty())
	        			id = "????";

	        		modif = String.format("[%s] %s", id, orig);
	        	}

	        	LanguageRegistry.instance().addStringLocalization(key, modif);
	        }
        }
        */

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ModLoader.addCommand(new CommandDumpHandlers());
		}
    }

	@Override
	public void modsLoaded() {
		super.modsLoaded();

		// LOAD COMPLETE
		proxy.registerMods();
		proxy.registerIMCs();
	}

	@Mod.IMCCallback
	public void processIMC(FMLInterModComms.IMCEvent event)
	{
		for (IMCMessage imcMessage : event.getMessages()){
			if (!imcMessage.isStringMessage()) continue;

			if (imcMessage.key.equalsIgnoreCase("addconfig")){
				String[] params = imcMessage.getStringValue().split("\\$\\$");
				if (params.length != 3){
					mod_BlockHelper.log.warning(String.format("Error while parsing config option from [ %s ] for %s", imcMessage.getSender(), imcMessage.getStringValue()));
					continue;
				}
				mod_BlockHelper.log.info(String.format("Receiving config request from [ %s ] for %s", imcMessage.getSender(), imcMessage.getStringValue()));
				ConfigHandler.instance().addConfig(params[0], params[1], params[2]);
			}

			if (imcMessage.key.equalsIgnoreCase("register")){
				mod_BlockHelper.log.info(String.format("Receiving registration request from [ %s ] for method %s", imcMessage.getSender(), imcMessage.getStringValue()));
				ModuleRegistrar.instance().addIMCRequest(imcMessage.getStringValue(), imcMessage.getSender());
			}
		}
	}

}
