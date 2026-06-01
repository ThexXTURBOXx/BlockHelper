package mcp.mobius.waila.addons.advmachines.as;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class AdvMachinesASPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvMachinesASPlugin();

    public static Class<?> TileEntityBaseMachine = null;
    public static Field TileEntityBaseMachine_energy = null;
    public static Field TileEntityBaseMachine_maxEnergy = null;

    private AdvMachinesASPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_IC2AdvancedMachines");
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines AS] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines AS] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            TileEntityBaseMachine = AccessHelper.getClass("ic2.advancedmachines.TileEntityBaseMachine");
            TileEntityBaseMachine_energy = AccessHelper.getField(TileEntityBaseMachine, "energy");
            TileEntityBaseMachine_maxEnergy = AccessHelper.getField(TileEntityBaseMachine, "maxEnergy");

            registrar.addSyncedConfig("Advanced Machines AS", "advmachines.storage");

            registrar.addConfig("Advanced Machines AS", "advmachines.energybars");

            registrar.registerNBTProvider(HUDHandlerAdvGeneratorAS.INSTANCE, TileEntityBaseMachine);

            registrar.registerBodyProvider(HUDHandlerAdvGeneratorAS.INSTANCE, TileEntityBaseMachine);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Machines AS] Error while loading generator hooks.", t);
        }
    }

}
