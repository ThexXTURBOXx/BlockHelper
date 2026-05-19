package mcp.mobius.waila.addons.ir;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class IntegratedRedstonePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new IntegratedRedstonePlugin();

    static Class<?> TileLogic = null;
    static Field TileLogic_Rotation = null;

    static Class<?> BlockLogic = null;

    private IntegratedRedstonePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_IntegratedRedstone");
            mod_BlockHelper.LOG.log(Level.INFO, "[Integrated Redstone] Mod found.");
            return true;
        } catch (Throwable ignored) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Integrated Redstone] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            BlockLogic = AccessHelper.getClass("eloraam.BlockLogic");

            TileLogic = AccessHelper.getClass("eloraam.TileLogic");
            TileLogic_Rotation = AccessHelper.getField(TileLogic, "Rotation");

            registrar.addConfig("Integrated Redstone", "pr.showio");
            registrar.addConfig("Integrated Redstone", "pr.showdata");

            registrar.registerDecorator(HUDDecoratorGateLogic.INSTANCE, BlockLogic);

            registrar.registerBodyProvider(HUDHandlerGateLogic.INSTANCE, BlockLogic);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Integrated Redstone] Error while loading gate hooks.", t);
        }
    }

}
