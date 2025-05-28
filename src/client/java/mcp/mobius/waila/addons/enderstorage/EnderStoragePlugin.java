package mcp.mobius.waila.addons.enderstorage;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class EnderStoragePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EnderStoragePlugin();

    public static Class<?> TileEnderChest = null;
    public static Field TileEnderChest_freq = null;

    public static Class<?> EnderStorageManager = null;
    public static Method GetColourFromFreq = null;

    private EnderStoragePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_EnderStorage");
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderStorage] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderStorage] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            TileEnderChest = AccessHelper.getClass("codechicken.enderstorage.TileEnderChest");
            TileEnderChest_freq = AccessHelper.getField(TileEnderChest, "freq");

            EnderStorageManager = AccessHelper.getClass("codechicken.enderstorage.EnderStorageManager");
            GetColourFromFreq = AccessHelper.getDeclaredMethod(EnderStorageManager, new Class[]{int.class, int.class},
                    "getColourFromFreq");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderStorage] Class not found.", e);
            return;
        } catch (NoSuchMethodException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderStorage] Method not found.", e);
            return;
        } catch (NoSuchFieldException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderStorage] Field not found.", e);
            return;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EnderStorage] Unhandled exception.", t);
            return;
        }

        registrar.addConfig("EnderStorage", "enderstorage.colors");

        registrar.registerBodyProvider(HUDHandlerFrequency.INSTANCE, TileEnderChest);
    }

}
