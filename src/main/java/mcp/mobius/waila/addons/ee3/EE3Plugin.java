package mcp.mobius.waila.addons.ee3;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.block.Block;
import net.minecraft.src.mod_BlockHelper;

public final class EE3Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EE3Plugin();

    public static Method EMCRegistry_instance = null;
    public static Method EMCRegistry_getEMCValue = null;
    public static Method EMCEntry_getCost = null;

    private EE3Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("com.pahimar.ee3.EquivalentExchange3");
            mod_BlockHelper.LOG.log(Level.INFO, "[EE3] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EE3] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            Class<?> EMCRegistry = AccessHelper.getClass("com.pahimar.ee3.emc.EMCRegistry");
            EMCRegistry_instance = AccessHelper.getMethod(EMCRegistry, new Class[0],
                    "instance");
            EMCRegistry_getEMCValue = AccessHelper.getMethod(EMCRegistry, new Class[]{int.class, int.class},
                    "getEMCValue");
            Class<?> EMCEntry = AccessHelper.getClass("com.pahimar.ee3.emc.EMCEntry");
            EMCEntry_getCost = AccessHelper.getMethod(EMCEntry, new Class[0],
                    "getCost");

            registrar.addConfig("Equivalent Exchange 3", "ee3.emc");

            registrar.registerBodyProvider(HUDHandlerEMC.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE3] Error while loading EMC hooks.", t);
        }
    }

}
