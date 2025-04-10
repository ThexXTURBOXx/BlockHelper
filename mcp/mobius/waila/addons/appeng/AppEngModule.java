package mcp.mobius.waila.addons.appeng;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;

public class AppEngModule {

    public static Class<?> IMEPowerStorage = null;
    public static Method IMEPowerStorage_currentPower = null;
    public static Method IMEPowerStorage_maxPower = null;

    public static void register() {
        try {
            IMEPowerStorage = Class.forName("appeng.api.me.tiles.IMEPowerStorage");
            IMEPowerStorage_currentPower = IMEPowerStorage.getMethod("getMECurrentPower");
            IMEPowerStorage_maxPower = IMEPowerStorage.getMethod("getMEMaxPower");

            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTEGenerator(), IMEPowerStorage);

            ModuleRegistrar.instance().registerNBTProvider(new HUDHandlerTEGenerator(), IMEPowerStorage);

            ModuleRegistrar.instance().addConfigRemote("Applied Energistics", "appeng.storage");

        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Applied Energistics] Error while loading generator hooks." + e);
        }
    }

}
