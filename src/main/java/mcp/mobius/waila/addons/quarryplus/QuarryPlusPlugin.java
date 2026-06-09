package mcp.mobius.waila.addons.quarryplus;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.addons.forge.TankRemover;
import mcp.mobius.waila.addons.forge.UniDirectionalTankFixer;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;

public final class QuarryPlusPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new QuarryPlusPlugin();

    private QuarryPlusPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("com.yogpc.qp.QuarryPlus");
            mod_BlockHelper.LOG.log(Level.INFO, "[QuarryPlus] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[QuarryPlus] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            registrar.registerTankProvider(UniDirectionalTankFixer.withDirectionOverride(ForgeDirection.UNKNOWN),
                    AccessHelper.getClass("com.yogpc.qp.tile.TileRefinery"));
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while fixing refinery tanks.", t);
        }

        try {
            // This is so badly coded... I don't even know how to make this mess work...
            registrar.registerTankProvider(TankRemover.INSTANCE, AccessHelper.getClass("com.yogpc.qp.tile.TilePump"));
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while fixing pump tanks.", t);
        }
    }

}
