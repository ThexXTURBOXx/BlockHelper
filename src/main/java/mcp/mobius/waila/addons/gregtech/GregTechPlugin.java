package mcp.mobius.waila.addons.gregtech;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.addons.forge.UniDirectionalTankFixer;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraftforge.common.ForgeDirection;

public final class GregTechPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new GregTechPlugin();

    private GregTechPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("gregtechmod.GT_Mod");
            mod_BlockHelper.LOG.log(Level.INFO, "[GregTech] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[GregTech] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            registrar.registerTankProvider(UniDirectionalTankFixer.withDirectionOverride(ForgeDirection.UNKNOWN),
                    AccessHelper.getClass("gregtechmod.api.metatileentity.BaseMetaTileEntity"));
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[GregTech] Error while fixing tanks.", t);
        }
    }

}
