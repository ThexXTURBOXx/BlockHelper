package mcp.mobius.waila.addons.barrels;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class BarrelsPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BarrelsPlugin();

    static Class<?> TileBarrel;
    static Method TileBarrel_getInventorySize;

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
            TileBarrel = AccessHelper.getClass("need4speed402.mods.barrels.TileBarrel");
            TileBarrel_getInventorySize = AccessHelper.getMethod(TileBarrel, new Class[0],
                    "getInventorySize");

            registrar.addSyncedConfig("Barrels", "barrels.itemtype");
            registrar.addSyncedConfig("Barrels", "barrels.itemnumb");
            registrar.addSyncedConfig("Barrels", "barrels.space");

            registrar.registerNBTProvider(HUDHandlerBarrels.INSTANCE, TileBarrel);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerBarrels.INSTANCE, TileBarrel);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Barrels] Error while loading barrel hooks.", t);
        }
    }

}
