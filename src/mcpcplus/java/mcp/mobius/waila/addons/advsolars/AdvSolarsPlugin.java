package mcp.mobius.waila.addons.advsolars;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.server.mod_BlockHelper;

public final class AdvSolarsPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvSolarsPlugin();

    public static Class<?> TileEntitySolarPanel = null;
    public static Field TileEntitySolarPanel_storage = null;
    public static Field TileEntitySolarPanel_maxStorage = null;

    public static Class<?> TileEntityQGenerator = null;
    public static Field TileEntityQGenerator_production = null;
    public static Field TileEntityQGenerator_maxPacketSize = null;

    private AdvSolarsPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("advsolar.AdvancedSolarPanel");
            mod_BlockHelper.LOG.log(Level.INFO, "[Advanced Solar Panels] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Advanced Solar Panels] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileEntitySolarPanel = AccessHelper.getClass("advsolar.TileEntitySolarPanel");
            TileEntitySolarPanel_storage = AccessHelper.getField(TileEntitySolarPanel, "storage");
            TileEntitySolarPanel_maxStorage = AccessHelper.getField(TileEntitySolarPanel, "maxStorage");

            registrar.addSyncedConfig("Advanced Solar Panels", "advsolars.storage");

            registrar.registerNBTProvider(HUDHandlerAdvSolars.INSTANCE, TileEntitySolarPanel);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks.", t);
        }

        try {
            TileEntityQGenerator = AccessHelper.getClass("advsolar.TileEntityQGenerator");
            TileEntityQGenerator_production = AccessHelper.getField(TileEntityQGenerator, "production");
            TileEntityQGenerator_maxPacketSize = AccessHelper.getField(TileEntityQGenerator, "maxPacketSize");

            registrar.addSyncedConfig("Advanced Solar Panels", "advsolars.qproduction");

            registrar.registerNBTProvider(HUDHandlerAdvSolars.INSTANCE, TileEntityQGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks.", t);
        }
    }

}
