package mcp.mobius.waila.addons.enderstorage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

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
            Class.forName("codechicken.enderstorage.EnderStorage");
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderStorage] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EnderStorage] Mod not found.");
        }
        return false;
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        try {
            TileFrequencyOwner = Class.forName("codechicken.enderstorage.common.TileFrequencyOwner");
            TileFrequencyOwner_Freq = TileFrequencyOwner.getField("freq");

            EnderStorageManager = Class.forName("codechicken.enderstorage.api.EnderStorageManager");
            GetColourFromFreq = EnderStorageManager.getDeclaredMethod("getColourFromFreq", Integer.TYPE, Integer.TYPE);

            TileEnderTank = Class.forName("codechicken.enderstorage.storage.liquid.TileEnderTank");
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
