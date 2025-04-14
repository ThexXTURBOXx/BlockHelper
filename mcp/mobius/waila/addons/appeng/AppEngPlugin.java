package mcp.mobius.waila.addons.appeng;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class AppEngPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AppEngPlugin();

    public static Class<?> IMEPowerStorage = null;
    public static Method IMEPowerStorage_currentPower = null;
    public static Method IMEPowerStorage_maxPower = null;

    private AppEngPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            IMEPowerStorage = Class.forName("appeng.api.me.tiles.IMEPowerStorage");
            IMEPowerStorage_currentPower = IMEPowerStorage.getMethod("getMECurrentPower");
            IMEPowerStorage_maxPower = IMEPowerStorage.getMethod("getMEMaxPower");

            registrar.registerBodyProvider(new HUDHandlerTEGenerator(), IMEPowerStorage);

            registrar.registerNBTProvider(new HUDHandlerTEGenerator(), IMEPowerStorage);

            registrar.addConfigRemote("Applied Energistics", "appeng.storage");

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Applied Energistics] Error while loading generator hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
