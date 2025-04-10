package mcp.mobius.waila.addons.advsolars;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;

public class AdvSolarsModule {

    public static Class<?> TileEntitySolarPanel = null;
    public static Field TileEntitySolarPanel_storage = null;
    public static Field TileEntitySolarPanel_maxStorage = null;

    public static Class<?> TileEntityQGenerator = null;
    public static Field TileEntityQGenerator_production = null;
    public static Field TileEntityQGenerator_maxPacketSize = null;

    public static void register() {
        try {
            TileEntitySolarPanel = Class.forName("advsolar.TileEntitySolarPanel");
            TileEntitySolarPanel_storage = TileEntitySolarPanel.getField("storage");
            TileEntitySolarPanel_maxStorage = TileEntitySolarPanel.getField("maxStorage");

            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTEGenerator(), TileEntitySolarPanel);

            ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerTEGenerator(), TileEntitySolarPanel);

            ModuleRegistrar.instance().addConfigRemote("Advanced Solar Panels", "advsolars.storage");

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks." + e);
        }

        try {
            TileEntityQGenerator = Class.forName("advsolar.TileEntityQGenerator");
            TileEntityQGenerator_production = TileEntityQGenerator.getField("production");
            TileEntityQGenerator_maxPacketSize = TileEntityQGenerator.getField("maxPacketSize");

            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTEGenerator(), TileEntityQGenerator);

            ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerTEGenerator(), TileEntityQGenerator);

            ModuleRegistrar.instance().addConfigRemote("Advanced Solar Panels", "advsolars.qproduction");

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Advanced Solar Panels] Error while loading generator hooks." + e);
        }
    }

}
