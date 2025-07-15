package mcp.mobius.waila.addons.projectzulu;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class ProjectZuluPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ProjectZuluPlugin();

    private ProjectZuluPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("projectzulu.common.ProjectZulu_Core");
            mod_BlockHelper.LOG.log(Level.INFO, "[Project Zulu] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Project Zulu] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            Class<?> TileEntityUniversalFlowerPot =
                    AccessHelper.getClass("projectzulu.common.blocks.TileEntityUniversalFlowerPot");

            registrar.registerStackProvider(HUDHandlerFlowerPot.INSTANCE, TileEntityUniversalFlowerPot);

            registrar.registerBodyProvider(HUDHandlerFlowerPot.INSTANCE, TileEntityUniversalFlowerPot);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Project Zulu] Error while loading flower pot hooks.", t);
        }
    }

}
