package mcp.mobius.waila.addons.advmachines.zippinus;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class AdvMachinesZippinusPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvMachinesZippinusPlugin();

    public static Class<?> TileEntityBaseMachine = null;
    public static Field TileEntityBaseMachine_energy = null;
    public static Field TileEntityBaseMachine_maxEnergy = null;

    private AdvMachinesZippinusPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_IC2AdvancedMachines");
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Zippinus] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Zippinus] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            TileEntityBaseMachine = AccessHelper.getClass("ic2.advancedmachines.TileEntityBaseMachine");
            TileEntityBaseMachine_energy = AccessHelper.getField(TileEntityBaseMachine, "energy");
            TileEntityBaseMachine_maxEnergy = AccessHelper.getField(TileEntityBaseMachine, "maxEnergy");

            registrar.addSyncedConfig("Advanced Machines Zippinus", "advmachines.storage");
            registrar.addConfig("Advanced Machines Zippinus", "advmachines.energybars");

            registrar.registerNBTProvider(HUDHandlerAdvGeneratorZippinus.INSTANCE, TileEntityBaseMachine);

            registrar.registerBodyProvider(HUDHandlerAdvGeneratorZippinus.INSTANCE, TileEntityBaseMachine);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING,
                    "[Advanced Machines Zippinus] Error while loading generator hooks.", t);
        }
    }

}
