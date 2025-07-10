package mcp.mobius.waila.addons.enderchest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class EnderChestPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EnderChestPlugin();

    public static Class<?> TileEnderChest = null;
    public static Field TileEnderChest_freq = null;

    public static Class<?> EnderStorageManager = null;
    public static Method GetColourFromFreq = null;

    private EnderChestPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_EnderChest");
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderChest] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderChest] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            TileEnderChest = AccessHelper.getClass("codechicken.enderchest.TileEnderChest");
            TileEnderChest_freq = AccessHelper.getField(TileEnderChest, "freq");

            EnderStorageManager = AccessHelper.getClass("codechicken.enderchest.EnderChestManager");
            GetColourFromFreq = AccessHelper.getDeclaredMethod(EnderStorageManager, new Class[]{int.class, int.class},
                    "getColourFromFreq");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderChest] Class not found.", e);
            return;
        } catch (NoSuchMethodException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderChest] Method not found.", e);
            return;
        } catch (NoSuchFieldException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderChest] Field not found.", e);
            return;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderChest] Unhandled exception.", t);
            return;
        }

        registrar.addConfig("EnderChest", "enderstorage.colors");

        registrar.registerBodyProvider(HUDHandlerFrequency.INSTANCE, TileEnderChest);
    }

}
