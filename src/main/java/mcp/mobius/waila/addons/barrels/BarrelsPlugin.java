package mcp.mobius.waila.addons.barrels;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class BarrelsPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BarrelsPlugin();

    static Class<?> TileEntityBarrel;
    static Method TileEntityBarrel_getInventorySize;
    static Method TileEntityBarrel_getStackLimit;

    private BarrelsPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("need4speed402.mods.barrels.Barrels");
            mod_BlockHelper.LOG.log(Level.INFO, "[Barrels] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Barrels] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileEntityBarrel = AccessHelper.getClass("need4speed402.mods.barrels.TileEntityBarrel");
            TileEntityBarrel_getInventorySize = AccessHelper.getMethod(TileEntityBarrel, new Class[0],
                    "getInventorySize");
            TileEntityBarrel_getStackLimit = AccessHelper.getMethod(TileEntityBarrel, new Class[0],
                    "getStackLimit");

            registrar.addSyncedConfig("Barrels", "barrels.itemtype");
            registrar.addSyncedConfig("Barrels", "barrels.itemnumb");
            registrar.addSyncedConfig("Barrels", "barrels.space");

            registrar.registerNBTProvider(HUDHandlerBarrels.INSTANCE, TileEntityBarrel);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerBarrels.INSTANCE, TileEntityBarrel);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Barrels] Error while loading barrel hooks.", t);
        }
    }

}
