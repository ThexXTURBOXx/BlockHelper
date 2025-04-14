package mcp.mobius.waila.addons.buildcraft;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class BCPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BCPlugin();

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

    private BCPlugin() {
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

            registrar.addConfigRemote("Buildcraft", "bcapi.storage");
            registrar.registerBodyProvider(new HUDHandlerBCEnergy(), IPowerReceptor);
            registrar.registerNBTProvider(new HUDHandlerBCEnergy(), IPowerReceptor);

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC] Error while loading Energy hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
