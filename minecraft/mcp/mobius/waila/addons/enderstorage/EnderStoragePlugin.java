package mcp.mobius.waila.addons.enderstorage;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class EnderStoragePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EnderStoragePlugin();

    public static Class<?> TileFrequencyOwner = null;
    public static Field TileFrequencyOwner_Freq = null;

    public static Class<?> EnderStorageManager = null;
    public static Method GetColourFromFreq = null;

    public static Class<?> TileEnderTank = null;

    private EnderStoragePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("codechicken.enderstorage.EnderStorage");
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
            TileFrequencyOwner = AccessHelper.getClass("codechicken.enderstorage.common.TileFrequencyOwner");
            TileFrequencyOwner_Freq = AccessHelper.getField(TileFrequencyOwner, "freq");

            EnderStorageManager = AccessHelper.getClass("codechicken.enderstorage.api.EnderStorageManager");
            GetColourFromFreq = AccessHelper.getDeclaredMethod(EnderStorageManager, new Class[]{int.class, int.class},
                    "getColourFromFreq");

            TileEnderTank = AccessHelper.getClass("codechicken.enderstorage.storage.liquid.TileEnderTank");
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

        registrar.registerBodyProvider(HUDHandlerFrequency.INSTANCE, TileFrequencyOwner);
    }

}
