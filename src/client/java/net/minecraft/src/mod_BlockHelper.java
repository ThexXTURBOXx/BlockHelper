package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import forge.Configuration;
import forge.MinecraftForge;
import forge.MinecraftForgeClient;
import forge.NetworkMod;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.client.ConfigKeyHandler;
import mcp.mobius.waila.network.WailaConnectionHandler;
import mcp.mobius.waila.overlay.DecoratorRenderer;
import mcp.mobius.waila.overlay.NEIOverlayRenderer;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.proxy.ProxyClient;
import mcp.mobius.waila.proxy.ProxyCommon;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.client.Minecraft;

public class mod_BlockHelper extends NetworkMod {

    public static final String PACKAGE = "mcp.mobius.waila.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "@MOD_VERSION@";
    public static final String MC_VERSION = "@MC_VERSION@";
    public static final String CHANNEL = "BlockHelper";
    public static final String CHANNEL_SSP = "BlockHelperSSP";
    public static final Logger LOG = Logger.getLogger(NAME);
    public static final BlockHelperUpdater UPDATER = new BlockHelperUpdater();
    public static mod_BlockHelper INSTANCE;
    public static ProxyCommon proxy;
    public static boolean DEV_MODE = false;

    static {
        LOG.setParent(ModLoader.getLogger());
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
        proxy = new ProxyClient();

        // PRE INIT
        I18n.INSTANCE.addLangDirFromHost(mod_BlockHelper.class, "/assets/waila/lang");

        new Thread(UPDATER, "Block Helper Version Check").start();

        Configuration cfg = new Configuration(new File(Minecraft.getMinecraftDir(), "config/BlockHelper.cfg"));
        PluginConfig.instance().loadDefaultConfig(cfg);
        OverlayConfig.updateColors();

        // INIT
        MinecraftForgeClient.registerRenderLastHandler(new DecoratorRenderer());
        MinecraftForgeClient.registerRenderLastHandler(new NEIOverlayRenderer());
        FMLCommonHandler.instance().registerTickHandler(new ConfigKeyHandler(this));
        FMLCommonHandler.instance().registerTickHandler(new WailaTickHandler());
        MinecraftForge.registerConnectionHandler(new WailaConnectionHandler());

        // POST INIT
        proxy.prepare();
        proxy.registerCorePlugins(WailaRegistrar.instance());
    }

    @Override
    public void modsLoaded() {
        // LOAD COMPLETE
        proxy.registerModPlugins(WailaRegistrar.instance());

        proxy.postLoad();
    }

    public static class Accessor {

        public static int damageDropped(Block b, int meta) {
            return b.damageDropped(meta);
        }

        @SuppressWarnings("unchecked")
        public static Entity getEntityByID(World w, int entityId) {
            if (w == null) return null;
            try {
                if (w instanceof WorldClient) {
                    Entity e = ((WorldClient) w).getEntityByID(entityId);
                    if (e != null)
                        return e;
                }
            } catch (Throwable ignored) {
            }
            List<Entity> list = (List<Entity>) w.getLoadedEntityList();
            if (list != null)
                for (Entity e : list)
                    if (e.entityId == entityId)
                        return e;
            return null;
        }

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
