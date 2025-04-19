package mcp.mobius.waila.addons.advmachines;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class AdvMachinesPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvMachinesPlugin();

    public static Class<?> TileAM2BaseGenerator = null;
    public static Field TileAM2BaseGenerator_stored = null;
    public static Field TileAM2BaseGenerator_maxStorage = null;

    private AdvMachinesPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("mods.immibis.am2.AdvancedMachines");
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileAM2BaseGenerator = Class.forName("mods.immibis.am2.TileAM2Base");
            TileAM2BaseGenerator_stored = TileAM2BaseGenerator.getDeclaredField("storedEnergy");
            TileAM2BaseGenerator_maxStorage = TileAM2BaseGenerator.getDeclaredField("MAX_STORAGE");
            TileAM2BaseGenerator_stored.setAccessible(true);
            TileAM2BaseGenerator_maxStorage.setAccessible(true);

            registrar.addSyncedConfig("Advanced Machines", "advmachines.storage");

            registrar.registerNBTProvider(HUDHandlerAdvGenerator.INSTANCE, TileAM2BaseGenerator);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerAdvGenerator.INSTANCE, TileAM2BaseGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Machines] Error while loading generator hooks.", t);
        }
    }

}
