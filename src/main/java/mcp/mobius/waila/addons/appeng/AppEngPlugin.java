package mcp.mobius.waila.addons.appeng;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class AppEngPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AppEngPlugin();

    public static Class<?> IMEPowerStorage = null;
    public static Method IMEPowerStorage_currentPower = null;
    public static Method IMEPowerStorage_maxPower = null;

    public static Class<?> TileStorageMonitor = null;
    public static Method TileStorageMonitor_getItem = null;

    public static Class<?> IAEItemStack = null;
    public static Method IAEItemStack_getItemStack = null;

    private AppEngPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("appeng.common.AppEng");
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
            IMEPowerStorage = AccessHelper.getClass("appeng.api.me.tiles.IMEPowerStorage");
            IMEPowerStorage_currentPower = AccessHelper.getMethod(IMEPowerStorage, new Class[0],
                    "getMECurrentPower");
            IMEPowerStorage_maxPower = AccessHelper.getMethod(IMEPowerStorage, new Class[0],
                    "getMEMaxPower");

            registrar.addSyncedConfig("Applied Energistics", "appeng.storage");

            if (side.isClient())
                registrar.addConfig("Applied Energistics", "appeng.energybars");

            registrar.registerNBTProvider(HUDHandlerMEPowerStorage.INSTANCE, IMEPowerStorage);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerMEPowerStorage.INSTANCE, IMEPowerStorage);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Applied Energistics] Error while loading generator hooks.", t);
        }

        try {
            TileStorageMonitor = AccessHelper.getClass("appeng.me.tile.TileStorageMonitor");
            IAEItemStack = AccessHelper.getClass("appeng.api.IAEItemStack");
            TileStorageMonitor_getItem = AccessHelper.getMethod(TileStorageMonitor, new Class[0],
                    "getItem");
            IAEItemStack_getItemStack = AccessHelper.getMethod(IAEItemStack, new Class[0],
                    "getItemStack");

            registrar.addSyncedConfig("Applied Energistics", "appeng.monitorcontent");

            registrar.registerNBTProvider(HUDAppEngMonitor.INSTANCE, TileStorageMonitor);

            if (side.isClient())
                registrar.registerBodyProvider(HUDAppEngMonitor.INSTANCE, TileStorageMonitor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Applied Energistics] Error while loading monitor hooks.", t);
        }
    }

}
