package mcp.mobius.waila.addons.advsolars;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

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
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("advsolar.AdvancedSolarPanel");
            mod_BlockHelper.LOG.log(Level.INFO, "[Advanced Solar Panels] Mod found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Advanced Solar Panels] Mod not found.");
            return;
        }

        try {
            TileEntitySolarPanel = Class.forName("advsolar.TileEntitySolarPanel");
            TileEntitySolarPanel_storage = TileEntitySolarPanel.getField("storage");
            TileEntitySolarPanel_maxStorage = TileEntitySolarPanel.getField("maxStorage");

            registrar.addSyncedConfig("Advanced Solar Panels", "advsolars.storage");

            registrar.registerBodyProvider(HUDHandlerAdvSolars.INSTANCE, TileEntitySolarPanel);
            registrar.registerNBTProvider(HUDHandlerAdvSolars.INSTANCE, TileEntitySolarPanel);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks.", t);
        }

        try {
            TileEntityQGenerator = Class.forName("advsolar.TileEntityQGenerator");
            TileEntityQGenerator_production = TileEntityQGenerator.getField("production");
            TileEntityQGenerator_maxPacketSize = TileEntityQGenerator.getField("maxPacketSize");

            registrar.addSyncedConfig("Advanced Solar Panels", "advsolars.qproduction");

            registrar.registerBodyProvider(HUDHandlerAdvSolars.INSTANCE, TileEntityQGenerator);
            registrar.registerNBTProvider(HUDHandlerAdvSolars.INSTANCE, TileEntityQGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
