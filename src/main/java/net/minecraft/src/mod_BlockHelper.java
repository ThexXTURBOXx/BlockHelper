package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import java.io.File;
import java.util.logging.Logger;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.client.ConfigKeyHandler;
import mcp.mobius.waila.network.WailaConnectionHandler;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.overlay.DecoratorRenderer;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.proxy.ProxyCommon;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

@NetworkMod(channels = {mod_BlockHelper.CHANNEL}, connectionHandler = WailaConnectionHandler.class,
        packetHandler = WailaPacketHandler.class)
public class mod_BlockHelper extends BaseMod {

    public static final String PACKAGE = "mcp.mobius.waila.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "@MOD_VERSION@";
    public static final String MC_VERSION = "@MC_VERSION@";
    public static final String CHANNEL = "BlockHelper";
    public static final Logger LOG = Logger.getLogger(NAME);
    public static final BlockHelperUpdater UPDATER = new BlockHelperUpdater();
    public static mod_BlockHelper INSTANCE;
    @SidedProxy(clientSide = PACKAGE + "proxy.ProxyClient", serverSide = PACKAGE + "proxy.ProxyServer")
    public static ProxyCommon proxy;
    public static boolean DEV_MODE = false;
    public static WailaTickHandler TICK_HANDLER;
    public static ConfigKeyHandler CONFIG_KEY_HANDLER;

    static {
        LOG.setParent(FMLLog.getLogger());
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

        // PRE INIT
        I18n.INSTANCE.addLangDirFromHost(mod_BlockHelper.class, "/assets/waila/lang");
        LanguageRegistry.reloadLanguageTable();

        new Thread(UPDATER, "Block Helper Version Check").start();

        Configuration cfg = new Configuration(new File((File) FMLInjectionData.data()[6], "config/BlockHelper.cfg"));
        PluginConfig.instance().loadDefaultConfig(cfg);
        OverlayConfig.updateColors();

        MinecraftForge.EVENT_BUS.register(new DecoratorRenderer());

        // INIT
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            CONFIG_KEY_HANDLER = new ConfigKeyHandler(this);
            TICK_HANDLER = new WailaTickHandler();
        }

        // POST INIT
        proxy.prepare();
        proxy.registerCorePlugins(WailaRegistrar.instance());
    }

    @Override
    public void modsLoaded() {
        // LOAD COMPLETE
        proxy.registerModPlugins(WailaRegistrar.instance());
        ModIdentification.init();
        proxy.postLoad();
    }

    @Override
    public boolean onTickInGame(float time, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return true;
        CONFIG_KEY_HANDLER.onTickInGame(mc);
        TICK_HANDLER.onTickInGame(mc);
        return true;
    }

    /**
     * If you want to register your plugin in a safe way, register it during the {@link #load()} phase
     * and use something like this:
     * <p><blockquote><pre>
     * try {
     *     Method register = Class.forName("mod_BlockHelper").getMethod("registerPlugin",
     *         Class.forName("mcp.mobius.waila.api.IWailaPlugin"));
     *     register.invoke(null, new PluginClass());
     * } catch (Throwable t) {
     *     t.printStackTrace(); // Proper logging or ignoring
     * }
     * </pre></blockquote><p>
     */
    public static void registerPlugin(IWailaPlugin plugin) {
        proxy.registerPlugin(plugin);
    }

}
