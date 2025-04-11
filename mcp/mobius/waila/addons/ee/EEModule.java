package mcp.mobius.waila.addons.ee;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.Block;

public class EEModule {

    public static Class<?> EMCRegistry = null;
    public static Method EMCRegistry_instance = null;
    public static Method EMCRegistry_getEMCValue = null;
    public static Class<?> EMCEntry = null;
    public static Method EMCEntry_getCost = null;

    public static void register() {
        try {
            EMCRegistry = Class.forName("com.pahimar.ee3.emc.EMCRegistry");
            EMCRegistry_instance = EMCRegistry.getMethod("instance");
            EMCRegistry_getEMCValue = EMCRegistry.getMethod("getEMCValue", int.class, int.class);
            EMCEntry = Class.forName("com.pahimar.ee3.emc.EMCEntry");
            EMCEntry_getCost = EMCEntry.getMethod("getCost");

            WailaRegistrar.instance().addConfig("Equivalent Exchange", "ee.emc");

            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerEE(), Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE] Error while loading EMC hooks.", t);
        }

    }

}
