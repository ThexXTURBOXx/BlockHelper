package mcp.mobius.waila.addons.bc2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class BC2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BC2Plugin();

    public static Class<?> TileEngine = null;
    public static Field TileEngine_engine = null;

    public static Class<?> Engine = null;
    public static Field Engine_energy = null;
    public static Field Engine_maxEnergy = null;

    public static Class<?> IPowerReceptor = null;
    public static Method IPowerReceptor_getPowerProvider = null;

    public static Class<?> PowerProvider = null;
    public static Field PowerProvider_energyStored = null;
    public static Field PowerProvider_maxEnergyStored = null;

    public static Class<?> ILiquidContainer = null;

    private BC2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_BuildCraftCore");
            mod_BlockHelper.LOG.log(Level.INFO, "[BC2] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[BC2] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            TileEngine = AccessHelper.getClass("buildcraft.energy.TileEngine");
            TileEngine_engine = AccessHelper.getField(TileEngine, "engine");

            Engine = AccessHelper.getClass("buildcraft.energy.Engine");
            Engine_energy = AccessHelper.getField(Engine, "energy");
            Engine_maxEnergy = AccessHelper.getField(Engine, "maxEnergy");

            IPowerReceptor = AccessHelper.getClass("buildcraft.api.IPowerReceptor");
            IPowerReceptor_getPowerProvider = AccessHelper.getMethod(IPowerReceptor, new Class[0],
                    "getPowerProvider");

            PowerProvider = AccessHelper.getClass("buildcraft.api.PowerProvider");
            PowerProvider_energyStored = AccessHelper.getField(PowerProvider, "energyStored");
            PowerProvider_maxEnergyStored = AccessHelper.getField(PowerProvider, "maxEnergyStored");

            registrar.addSyncedConfig("Buildcraft", "bcapi.storage");

            registrar.registerNBTProvider(HUDHandlerBC2Energy.INSTANCE, IPowerReceptor);

            registrar.registerBodyProvider(HUDHandlerBC2Energy.INSTANCE, IPowerReceptor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC2] Error while loading Energy hooks.", t);
        }

        try {
            ILiquidContainer = AccessHelper.getClass("buildcraft.api.ILiquidContainer");

            registrar.addSyncedConfig("Buildcraft", "bc.tankamount");
            registrar.addSyncedConfig("Buildcraft", "bc.tanktype");

            registrar.registerNBTProvider(HUDHandlerBC2Tanks.INSTANCE, ILiquidContainer);
            registrar.registerNBTProvider(HUDHandlerEntityBC2Tanks.INSTANCE, ILiquidContainer);

            registrar.registerHeadProvider(HUDHandlerBC2Tanks.INSTANCE, ILiquidContainer);
            registrar.registerHeadProvider(HUDHandlerEntityBC2Tanks.INSTANCE, ILiquidContainer);

            registrar.registerBodyProvider(HUDHandlerBC2Tanks.INSTANCE, ILiquidContainer);
            registrar.registerBodyProvider(HUDHandlerEntityBC2Tanks.INSTANCE, ILiquidContainer);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC2] Error while loading Tank hooks.", t);
        }
    }

}
