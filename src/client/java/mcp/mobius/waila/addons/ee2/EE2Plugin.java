package mcp.mobius.waila.addons.ee2;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.mod_BlockHelper;

public final class EE2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EE2Plugin();

    public static Method EEMaps_getEMC = null;

    private EE2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_EE");
            mod_BlockHelper.LOG.log(Level.INFO, "[EE2] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EE2] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            Class<?> EEMaps = AccessHelper.getClass("ee.EEMaps");
            EEMaps_getEMC = AccessHelper.getMethod(EEMaps, new Class[]{ItemStack.class}, "getEMC");

            registrar.addConfig("Equivalent Exchange 2", "ee2.emc");

            registrar.registerBodyProvider(HUDHandlerEMC.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE2] Error while loading EMC hooks.", t);
        }
    }

}
