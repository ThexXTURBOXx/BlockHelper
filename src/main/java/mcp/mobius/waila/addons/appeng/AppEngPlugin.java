package mcp.mobius.waila.addons.appeng;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class AppEngPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new AppEngPlugin();

    public static Class<?> TilePoweredBase = null;
    public static Field TilePoweredBase_storedPower = null;
    public static Field TilePoweredBase_maxStoredPower = null;

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
            TilePoweredBase = AccessHelper.getClass("appeng.me.basetiles.TilePoweredBase");
            TilePoweredBase_storedPower = AccessHelper.getDeclaredField(TilePoweredBase, "storedPower");
            TilePoweredBase_maxStoredPower = AccessHelper.getDeclaredField(TilePoweredBase, "maxStoredPower");

            registrar.addSyncedConfig("Applied Energistics", "appeng.storage");

            registrar.registerNBTProvider(HUDHandlerMEPowerStorage.INSTANCE, TilePoweredBase);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerMEPowerStorage.INSTANCE, TilePoweredBase);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Applied Energistics] Error while loading generator hooks.", t);
        }
    }

}
