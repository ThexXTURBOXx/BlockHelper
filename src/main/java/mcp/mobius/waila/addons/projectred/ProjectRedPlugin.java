package mcp.mobius.waila.addons.projectred;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class ProjectRedPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ProjectRedPlugin();

    static Class<?> BlockGate;

    static Class<?> TileRedAlloy;

    static Class<?> TileBundled;

    private ProjectRedPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("mrtjp.projectred.ProjectRed");
            mod_BlockHelper.LOG.log(Level.INFO, "[ProjectRed] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ProjectRed] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            BlockGate = AccessHelper.getClass("mrtjp.projectred.multipart.wiring.gates.BlockGate");

            registrar.addConfig("Project:Red", "pr.showio");
            registrar.addConfig("Project:Red", "pr.showdata");

            registrar.registerDecorator(HUDDecoratorGateLogic.INSTANCE, BlockGate);

            registrar.registerBodyProvider(HUDHandlerGateLogic.INSTANCE, BlockGate);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[ProjectRed] Error while loading gate hooks.", t);
        }

        try {
            TileRedAlloy = AccessHelper.getClass("mrtjp.projectred.multipart.wiring.wires.TileRedAlloy");
            TileBundled = AccessHelper.getClass("mrtjp.projectred.multipart.wiring.wires.TileBundled");

            registrar.addConfig("Project:Red", "pr.showsignal");

            registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileRedAlloy);
            registrar.registerBodyProvider(HUDHandlerWires.INSTANCE, TileBundled);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[ProjectRed] Error while loading wire hooks.", t);
        }
    }

}
