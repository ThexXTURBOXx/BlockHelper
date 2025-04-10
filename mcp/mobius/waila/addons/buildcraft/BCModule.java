package mcp.mobius.waila.addons.buildcraft;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;

public class BCModule {

    public static Class<?> TileTank = null;
    public static Method TileTank_getTanks = null;

    public static Class<?> IPowerReceptor = null;
    public static Method IPowerReceptor_getPowerProvider = null;
    public static Class<?> IPowerProvider = null;
    public static Method IPowerProvider_getEnergyStored = null;
    public static Method IPowerProvider_getMaxEnergyStored = null;

    public static void register() {
        try {
            TileTank = Class.forName("buildcraft.factory.TileTank");
            TileTank_getTanks = TileTank.getMethod("getTanks", ForgeDirection.class);

            ModuleRegistrar.instance().addConfig("Buildcraft", "bc.tankamount");
            ModuleRegistrar.instance().addConfig("Buildcraft", "bc.tanktype");
            ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerBCTanks(), TileTank);
            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerBCTanks(), TileTank);

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[BC] Error while loading Tank hooks." + e);
        }

        try {
            IPowerReceptor = Class.forName("buildcraft.api.power.IPowerReceptor");
            IPowerProvider = Class.forName("buildcraft.api.power.IPowerProvider");
            IPowerReceptor_getPowerProvider = IPowerReceptor.getMethod("getPowerProvider");
            IPowerProvider_getEnergyStored = IPowerProvider.getMethod("getEnergyStored");
            IPowerProvider_getMaxEnergyStored = IPowerProvider.getMethod("getMaxEnergyStored");

            ModuleRegistrar.instance().addConfigRemote("Buildcraft", "bcapi.storage");
            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerBCEnergy(), IPowerReceptor);
            ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerBCEnergy(), IPowerReceptor);

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[BC] Error while loading Energy hooks." + e);
        }

    }

}
