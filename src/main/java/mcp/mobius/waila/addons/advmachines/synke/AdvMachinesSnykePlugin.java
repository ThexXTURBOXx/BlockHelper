package mcp.mobius.waila.addons.advmachines.synke;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class AdvMachinesSnykePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AdvMachinesSnykePlugin();

    public static Class<?> TileAdvMachine = null;
    public static Field TileAdvMachine_energy = null;
    public static Field TileAdvMachine_maxEnergy = null;

    private AdvMachinesSnykePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("ic2.snyke7.AdvancedMachinesCore");
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Snyke] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[AdvancedMachines Snyke] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileAdvMachine = AccessHelper.getClass("ic2.snyke7.advMachine.TileAdvMachine");
            TileAdvMachine_energy = AccessHelper.getField(TileAdvMachine, "energy");
            TileAdvMachine_maxEnergy = AccessHelper.getField(TileAdvMachine, "maxEnergy");

            registrar.addSyncedConfig("Advanced Machines Snyke", "advmachines.storage");

            if (side.isClient())
                registrar.addConfig("Advanced Machines Snyke", "advmachines.energybars");

            registrar.registerNBTProvider(HUDHandlerAdvGeneratorSnyke.INSTANCE, TileAdvMachine);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerAdvGeneratorSnyke.INSTANCE, TileAdvMachine);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Advanced Machines Snyke] Error while loading generator hooks.", t);
        }
    }

}
