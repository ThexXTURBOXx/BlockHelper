package mcp.mobius.waila;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.util.logging.Logger;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.client.ConfigKeyHandler;
import mcp.mobius.waila.commands.CommandDumpHandlers;
import mcp.mobius.waila.network.WailaConnectionHandler;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.overlay.DecoratorRenderer;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.server.ProxyServer;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

@NetworkMod(channels = {mod_BlockHelper.CHANNEL}, connectionHandler = WailaConnectionHandler.class, packetHandler =
        WailaPacketHandler.class)
public class mod_BlockHelper extends BaseMod {

    public static final String PACKAGE = "mcp.mobius.waila.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "2.0.0-pre1";
    public static final String MC_VERSION = "1.5.2";
    public static final String CHANNEL = "BlockHelper";
    public static mod_BlockHelper INSTANCE;

    @SidedProxy(clientSide = PACKAGE + "client.ProxyClient", serverSide = PACKAGE + "server.ProxyServer")
    public static ProxyServer proxy;
    public static Logger log = Logger.getLogger(NAME);

    static {
        log.setParent(FMLLog.getLogger());
    }

    public boolean serverPresent = false;

    public static String getModId() {
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
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
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
        // LOAD COMPLETE
        proxy.registerMods();
    }

}
