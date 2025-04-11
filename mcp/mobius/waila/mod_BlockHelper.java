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
    public static final Logger LOG = Logger.getLogger(NAME);
    public static final BlockHelperUpdater UPDATER = new BlockHelperUpdater();
    public static mod_BlockHelper INSTANCE;
    @SidedProxy(clientSide = PACKAGE + "client.ProxyClient", serverSide = PACKAGE + "server.ProxyServer")
    public static ProxyServer proxy;
    public static boolean DEV_MODE = false;

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

        new Thread(UPDATER, "Block Helper Version Check").start();

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

        if (DEV_MODE) {
            ModLoader.addCommand(new CommandDumpHandlers());
        }
    }

    @Override
    public void modsLoaded() {
        // LOAD COMPLETE
        proxy.registerMods();
    }

}
