package mcp.mobius.waila.addons.harvestcraft;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.block.Block;
import net.minecraft.src.mod_BlockHelper;

public final class HarvestcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new HarvestcraftPlugin();

    private HarvestcraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("pamsmods.common.harvestcraft.PamHCBase");
            mod_BlockHelper.LOG.log(Level.INFO, "[PamHarvestCraft] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[PamHarvestCraft] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (!side.isClient()) return;

        try {
            registrar.registerStackProvider(HUDHandlerPamCrops.INSTANCE, Block.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[PamHarvestCraft] Error while loading crop hooks.", t);
        }
    }

}
