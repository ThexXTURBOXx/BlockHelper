package mcp.mobius.waila.addons.advmachines;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;

public class AdvMachinesModule {

    public static Class<?> TileAM2BaseGenerator = null;
    public static Field TileAM2BaseGenerator_stored = null;
    public static Field TileAM2BaseGenerator_maxStorage = null;

    public static void register() {
        try {
            TileAM2BaseGenerator = Class.forName("mods.immibis.am2.TileAM2Base");
            TileAM2BaseGenerator_stored = TileAM2BaseGenerator.getDeclaredField("storedEnergy");
            TileAM2BaseGenerator_maxStorage = TileAM2BaseGenerator.getDeclaredField("MAX_STORAGE");
            TileAM2BaseGenerator_stored.setAccessible(true);
            TileAM2BaseGenerator_maxStorage.setAccessible(true);

            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTEGenerator(), TileAM2BaseGenerator);

            ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerTEGenerator(), TileAM2BaseGenerator);

            ModuleRegistrar.instance().addConfigRemote("Advanced Machines", "advmachines.storage");

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Advanced Machines] Error while loading generator hooks." + e);
        }
    }

}
