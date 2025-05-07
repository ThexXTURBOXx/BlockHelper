package mcp.mobius.waila.addons.fmp;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class FMPPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new FMPPlugin();

    Class<?> BlockMultipart;

    @Override
    public boolean shouldRegister() {
        try {
            BlockMultipart = AccessHelper.getClass("codechicken.multipart.BlockMultipart");
            mod_BlockHelper.LOG.log(Level.INFO, "[Forge Multipart] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Forge Multipart] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            if (side.isClient()) {
                registrar.registerDecorator(DecoratorFMP.INSTANCE, BlockMultipart);

                registrar.registerHeadProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
                registrar.registerBodyProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
                registrar.registerTailProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Forge Multipart] Error while loading multipart hooks.", t);
        }
    }

}
