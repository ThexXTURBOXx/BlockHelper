package mcp.mobius.waila.addons.bc3;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class BC3Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BC3Plugin();

    public static Class<?> TileEngine = null;
    public static Field TileEngine_engine = null;
    public static Class<?> Engine = null;
    public static Field Engine_energy = null;
    public static Field Engine_maxEnergy = null;
    public static Class<?> IPowerReceptor = null;
    public static Method IPowerReceptor_getPowerProvider = null;
    public static Class<?> IPowerProvider = null;
    public static Method IPowerProvider_getEnergyStored = null;
    public static Method IPowerProvider_getMaxEnergyStored = null;

    private BC3Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("buildcraft.BuildCraftCore");
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileEngine = AccessHelper.getClass("buildcraft.energy.TileEngine");
            Engine = AccessHelper.getClass("buildcraft.energy.Engine");
            IPowerReceptor = AccessHelper.getClass("buildcraft.api.power.IPowerReceptor");
            IPowerProvider = AccessHelper.getClass("buildcraft.api.power.IPowerProvider");
            TileEngine_engine = AccessHelper.getField(TileEngine, "engine");
            Engine_energy = AccessHelper.getField(Engine, "energy");
            Engine_maxEnergy = AccessHelper.getField(Engine, "maxEnergy");
            IPowerReceptor_getPowerProvider = AccessHelper.getMethod(IPowerReceptor, new Class[0],
                    "getPowerProvider");
            IPowerProvider_getEnergyStored = AccessHelper.getMethod(IPowerProvider, new Class[0],
                    "getEnergyStored");
            IPowerProvider_getMaxEnergyStored = AccessHelper.getMethod(IPowerProvider, new Class[0],
                    "getMaxEnergyStored");

            registrar.addSyncedConfig("Buildcraft", "bcapi.storage");

            registrar.registerNBTProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while loading Energy hooks.", t);
        }
    }

}
