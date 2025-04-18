package mcp.mobius.waila.addons.ee;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.Block;

public final class EEPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EEPlugin();

    public static Class<?> EMCRegistry = null;
    public static Method EMCRegistry_instance = null;
    public static Method EMCRegistry_getEMCValue = null;
    public static Class<?> EMCEntry = null;
    public static Method EMCEntry_getCost = null;

    private EEPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("com.pahimar.ee3.EquivalentExchange3");
            mod_BlockHelper.LOG.log(Level.INFO, "[EE] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EE] Mod not found.");
        }
        return false;
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        try {
            EMCRegistry = Class.forName("com.pahimar.ee3.emc.EMCRegistry");
            EMCRegistry_instance = EMCRegistry.getMethod("instance");
            EMCRegistry_getEMCValue = EMCRegistry.getMethod("getEMCValue", int.class, int.class);
            EMCEntry = Class.forName("com.pahimar.ee3.emc.EMCEntry");
            EMCEntry_getCost = EMCEntry.getMethod("getCost");

            registrar.addConfig("Equivalent Exchange", "ee.emc");

            registrar.registerBodyProvider(HUDHandlerEMC.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE] Error while loading EMC hooks.", t);
        }
    }

}
