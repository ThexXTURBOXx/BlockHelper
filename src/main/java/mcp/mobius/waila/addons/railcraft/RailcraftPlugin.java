package mcp.mobius.waila.addons.railcraft;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.entity.item.EntityMinecart;

public final class RailcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new RailcraftPlugin();

    public static Class<?> TankWrapper;
    public static Field TankWrapper_tank;

    private RailcraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mods.railcraft.common.core.Railcraft");
            mod_BlockHelper.LOG.log(Level.INFO, "[Railcraft] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Railcraft] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            if (side.isClient())
                registrar.registerHeadProvider(HUDHandlerCarts.INSTANCE, EntityMinecart.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Railcraft] Error while loading cart hooks.", t);
        }

        try {
            TankWrapper = AccessHelper.getClass("mods.railcraft.common.liquids.TankWrapper");
            TankWrapper_tank = AccessHelper.getDeclaredField(TankWrapper, "tank");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Railcraft] Error while loading tank wrapper hooks.", t);
        }
    }

}
