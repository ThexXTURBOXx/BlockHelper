package mcp.mobius.waila.addons.bc3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

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
            Class.forName("buildcraft.BuildCraftCore");
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod not found.");
        }
        return false;
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            TileEngine = Class.forName("buildcraft.energy.TileEngine");
            Engine = Class.forName("buildcraft.energy.Engine");
            IPowerReceptor = Class.forName("buildcraft.api.power.IPowerReceptor");
            IPowerProvider = Class.forName("buildcraft.api.power.IPowerProvider");
            TileEngine_engine = TileEngine.getField("engine");
            Engine_energy = Engine.getField("energy");
            Engine_maxEnergy = Engine.getField("maxEnergy");
            IPowerReceptor_getPowerProvider = IPowerReceptor.getMethod("getPowerProvider");
            IPowerProvider_getEnergyStored = IPowerProvider.getMethod("getEnergyStored");
            IPowerProvider_getMaxEnergyStored = IPowerProvider.getMethod("getMaxEnergyStored");

            registrar.addSyncedConfig("Buildcraft", "bcapi.storage");

            registrar.registerNBTProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while loading Energy hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        registrar.registerBodyProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);
    }

}
