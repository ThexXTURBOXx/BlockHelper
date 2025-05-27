package mcp.mobius.waila.addons.railcraft;

import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.mod_BlockHelper;

public final class RailcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RailcraftPlugin();

    private RailcraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("railcraft.common.core.Railcraft");
            mod_BlockHelper.LOG.log(Level.INFO, "[Railcraft] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Railcraft] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            registrar.registerHeadProvider(HUDHandlerCarts.INSTANCE, EntityMinecart.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Railcraft] Error while loading cart hooks.", t);
        }
    }

}
