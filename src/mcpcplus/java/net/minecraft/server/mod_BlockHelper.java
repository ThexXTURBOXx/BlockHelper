package net.minecraft.server;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.network.Packet0x00ServerPing;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.proxy.ProxyCommon;
import mcp.mobius.waila.proxy.ProxyServer;
import mcp.mobius.waila.utils.BlockHelperUpdater;
import mcp.mobius.waila.utils.config.Configuration;

public class mod_BlockHelper extends BaseModMp {

    public static final String PACKAGE = "mcp.mobius.waila.";
    public static final String MOD_ID = "mod_BlockHelper";
    public static final String NAME = "Block Helper";
    public static final String VERSION = "@MOD_VERSION@";
    public static final String MC_VERSION = "@MC_VERSION@";
    public static final String CHANNEL = "BlockHelper";
    public static final Logger LOG = Logger.getLogger(NAME);
    public static final BlockHelperUpdater UPDATER = new BlockHelperUpdater();
    public static mod_BlockHelper INSTANCE;
    public static ProxyCommon proxy;

    static {
        LOG.setParent(ModLoader.getLogger());
    }

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
        proxy = new ProxyServer();

        // PRE INIT
        new Thread(UPDATER, "Block Helper Version Check").start();

        File cfgdir;
        try {
            String supdir = ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            supdir = supdir.substring(0, supdir.lastIndexOf(47));
            cfgdir = new File(supdir, "/config/");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        Configuration cfg = new Configuration(new File(cfgdir, "BlockHelper.cfg"));
        PluginConfig.instance().loadDefaultConfig(cfg);

        // INIT

        // POST INIT
        proxy.prepare();
        proxy.registerCorePlugins(WailaRegistrar.instance());
    }

    @Override
    public void modsLoaded() {
        // LOAD COMPLETE
        super.modsLoaded();
        proxy.registerModPlugins(WailaRegistrar.instance());

        proxy.postLoad();
    }

    @Override
    public void handleLogin(EntityPlayer player) {
        WailaPacketHandler.sendPacketToPlayer(new Packet0x00ServerPing(), player);
    }

    @Override
    public void handlePacket(Packet230ModLoader payload, EntityPlayer source) {
        WailaPacketHandler.INSTANCE.onPacketData(source, payload);
    }

    public static class Accessor {

        public static int damageDropped(Block b, int meta) {
            return b.getDropData(meta);
        }

        @SuppressWarnings("unchecked")
        public static Entity getEntityByID(World w, int entityId) {
            if (w == null) return null;
            try {
                if (w instanceof WorldServer) {
                    Entity e = ((WorldServer) w).getEntity(entityId);
                    if (e != null)
                        return e;
                }
            } catch (Throwable ignored) {
            }
            List<Entity> list = (List<Entity>) w.entityList;
            if (list != null)
                for (Entity e : list)
                    if (e.id == entityId)
                        return e;
            return null;
        }

    }

    /**
     * If you want to register your plugin in a safe way, register it during the {@link #load()} phase
     * and use something like this:
     * <p><blockquote><pre>
     * try {
     *     Method register = Class.forName("net.minecraft.server.mod_BlockHelper").getMethod("registerPlugin",
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
