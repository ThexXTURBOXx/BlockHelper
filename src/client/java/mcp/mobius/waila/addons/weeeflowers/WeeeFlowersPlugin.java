package mcp.mobius.waila.addons.weeeflowers;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class WeeeFlowersPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new WeeeFlowersPlugin();

    static Class<?> PamWeeeFlowers;

    private WeeeFlowersPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            PamWeeeFlowers = AccessHelper.getClass("pamsmods.common.weeeflowers.PamWeeeFlowers");
            mod_BlockHelper.LOG.log(Level.INFO, "[PamWeeeFlowers] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[PamWeeeFlowers] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        // Yes, this is more than just ugly... But Pam's code here is more than just ugly as well...
        try {
            for (Field f : PamWeeeFlowers.getFields()) {
                String name = f.getName();
                if (name.startsWith("pam") && name.endsWith("flowerCrop")) {
                    Object flowerCrop = f.get(null);
                    if (flowerCrop != null)
                        registrar.registerStackProvider(HUDHandlerWeeeCrops.INSTANCE, flowerCrop.getClass());
                }
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[PamWeeeFlowers] Error while loading crop hooks.", t);
        }
    }

}
