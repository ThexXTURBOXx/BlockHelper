package net.minecraft.src;

import java.io.File;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.client.ConfigKeyHandler;
import mcp.mobius.waila.network.Packet0x00ServerPing;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.overlay.WailaTickHandler;
import mcp.mobius.waila.proxy.ProxyClient;
import mcp.mobius.waila.proxy.ProxyCommon;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.config.Configuration;
import mcp.mobius.waila.utils.log.FMLLikeLogFormatter;
import net.minecraft.client.Minecraft;

public class mod_BlockHelper extends BaseMod {

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
    public static WailaTickHandler TICK_HANDLER;
    public static ConfigKeyHandler CONFIG_KEY_HANDLER;

    static {
        ConsoleHandler ch = new ConsoleHandler();
        LOG.setUseParentHandlers(false);
        LOG.addHandler(ch);
        ch.setFormatter(new FMLLikeLogFormatter());
    }

    public boolean serverPresent = false;

    public static String getModId() {
        return MOD_ID;
    }

    public String Version() {
        return VERSION;
    }

    @Override
    public void ModsLoaded() {
        // LOAD COMPLETE
        super.ModsLoaded();

        INSTANCE = this;
        proxy = new ProxyClient();

        // PRE INIT
        I18n.INSTANCE.addDefaultLangFromHost(mod_BlockHelper.class, "/assets/waila/lang");

        new Thread(UPDATER, "Block Helper Version Check").start();

        Configuration cfg = new Configuration(new File(Minecraft.getMinecraftDir(), "config/BlockHelper.cfg"));
        PluginConfig.instance().loadDefaultConfig(cfg);
        OverlayConfig.updateColors();

        // INIT
        CONFIG_KEY_HANDLER = new ConfigKeyHandler();
        TICK_HANDLER = new WailaTickHandler();

        // POST INIT
        proxy.prepare();

        WailaRegistrar registrar = WailaRegistrar.instance();
        proxy.registerCorePlugins(registrar);
        proxy.registerModPlugins(registrar);

        proxy.postLoad();
    }

    @Override
    public void OSDHook(Minecraft mc, boolean guiOpen) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            CONFIG_KEY_HANDLER.onTickInGame(mc);
            TICK_HANDLER.onTickInGame(mc);
        }

        if (serverPresent && mc.theWorld == null)
            Packet0x00ServerPing.resetClient();
    }

    /*@Override
    public void HandlePacket(Packet200ModLoader packet) {
        WailaPacketHandler.INSTANCE.onPacketData(packet);
    }*/

    public static class Accessor {

        @SuppressWarnings("unchecked")
        public static Entity getEntityByID(World w, int entityId) {
            if (w == null) return null;
            try {
                if (w instanceof WorldClient) {
                    Entity e = ((WorldClient) w).func_709_b(entityId);
                    if (e != null)
                        return e;
                }
            } catch (Throwable ignored) {
            }
            List<Entity> list = (List<Entity>) w.func_658_i();
            if (list != null)
                for (Entity e : list)
                    if (e.field_620_ab == entityId)
                        return e;
            return null;
        }

        public static float getHardness(Block b) {
            return b.blockHardness;
        }

    }

    /**
     * If you want to register your plugin in a safe way, register it during the {@link #ModsLoaded()} phase
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
