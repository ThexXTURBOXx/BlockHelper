package mcp.mobius.waila.addons.harvestcraft;

import java.util.logging.Level;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class HarvestcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new HarvestcraftPlugin();

    private HarvestcraftPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("mods.PamHarvestCraft.PamHarvestCraft");
            mod_BlockHelper.LOG.log(Level.INFO, "[PamHarvestCraft] Mod found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[PamHarvestCraft] Mod not found.");
            return;
        }

        try {
            Class<?> BlockPamCrop = Class.forName("mods.PamHarvestCraft.BlockPamCrop");
            VanillaPlugin.MAX_STAGES.put(BlockPamCrop, 7);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[PamHarvestCraft] Error while loading crop hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
