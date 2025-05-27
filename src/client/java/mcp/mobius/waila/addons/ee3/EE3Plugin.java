package mcp.mobius.waila.addons.ee3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.mod_BlockHelper;

public final class EE3Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EE3Plugin();

    public static Class<?> mod_EE3;
    public static Field mod_EE3_emcList = null;

    public static Field EMCList_emcMap = null;

    public static Method EMCValue_getCostEMC = null;

    private EE3Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            mod_EE3 = AccessHelper.getClass("ee3.mod_EE3");
            mod_BlockHelper.LOG.log(Level.INFO, "[EE3] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EE3] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            Class<?> EMCList = AccessHelper.getClass("ee3.emc.EMCList");
            EMCList_emcMap = AccessHelper.getDeclaredField(EMCList, "emcMap");
            Class<?> EMCValue = AccessHelper.getClass("ee3.emc.EMCValue");
            EMCValue_getCostEMC = AccessHelper.getMethod(EMCValue, new Class[0],
                    "getCostEMC");

            registrar.addConfig("Equivalent Exchange 3", "ee3.emc");

            registrar.registerBodyProvider(HUDHandlerEMC.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE3] Error while loading EMC hooks.", t);
        }
    }

}
