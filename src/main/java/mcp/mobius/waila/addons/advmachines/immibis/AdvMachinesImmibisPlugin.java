package mcp.mobius.waila.addons.advmachines.immibis;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class AdvMachinesImmibisPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvMachinesImmibisPlugin();

    public static Class<?> TileAM2BaseGenerator = null;
    public static Field TileAM2BaseGenerator_stored = null;
    public static Field TileAM2BaseGenerator_maxStorage = null;

    private AdvMachinesImmibisPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mods.immibis.am2.AdvancedMachines");
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Immibis] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Immibis] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileAM2BaseGenerator = AccessHelper.getClass("mods.immibis.am2.TileAM2Base");
            TileAM2BaseGenerator_stored = AccessHelper.getDeclaredField(TileAM2BaseGenerator, "storedEnergy");
            TileAM2BaseGenerator_maxStorage = AccessHelper.getDeclaredField(TileAM2BaseGenerator, "MAX_STORAGE");

            registrar.addSyncedConfig("Advanced Machines Immibis", "advmachines.storage");

            if (side.isClient())
                registrar.addConfig("Advanced Machines Immibis", "advmachines.energybars");

            registrar.registerNBTProvider(HUDHandlerAdvGenerator.INSTANCE, TileAM2BaseGenerator);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerAdvGenerator.INSTANCE, TileAM2BaseGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Machines Immibis] Error while loading generator hooks.", t);
        }
    }

}
