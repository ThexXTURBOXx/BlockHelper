package mcp.mobius.waila.addons.ee;

import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.mod_BlockHelper;

public final class EEPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new EEPlugin();

    public static Method mod_EE_getDamagedAlchemicalValue = null;

    private EEPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class<?> mod_EE = AccessHelper.getClass("mod_EE");
            mod_EE_getDamagedAlchemicalValue = AccessHelper.getMethod(mod_EE, new Class[]{int.class, int.class},
                    "getDamagedAlchemicalValue");
            mod_BlockHelper.LOG.log(Level.INFO, "[EE] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[EE] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            registrar.addConfig("Equivalent Exchange", "ee2.emc");

            registrar.registerBodyProvider(HUDHandlerEMC.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[EE] Error while registering EMC hooks.", t);
        }
    }

}
