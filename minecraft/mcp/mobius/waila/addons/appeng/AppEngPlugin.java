package mcp.mobius.waila.addons.appeng;

import cpw.mods.fml.relauncher.Side;
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
    public boolean shouldRegister() {
        try {
            Class.forName("appeng.common.AppEng");
            mod_BlockHelper.LOG.log(Level.INFO, "[Applied Energistics] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Applied Energistics] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            IMEPowerStorage = Class.forName("appeng.api.me.tiles.IMEPowerStorage");
            IMEPowerStorage_currentPower = IMEPowerStorage.getMethod("getMECurrentPower");
            IMEPowerStorage_maxPower = IMEPowerStorage.getMethod("getMEMaxPower");

            registrar.addSyncedConfig("Applied Energistics", "appeng.storage");

            registrar.registerNBTProvider(HUDHandlerMEPowerStorage.INSTANCE, IMEPowerStorage);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerMEPowerStorage.INSTANCE, IMEPowerStorage);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Applied Energistics] Error while loading generator hooks.", t);
        }
    }

}
