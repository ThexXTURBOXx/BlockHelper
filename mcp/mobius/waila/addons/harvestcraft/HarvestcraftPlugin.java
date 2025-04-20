package mcp.mobius.waila.addons.harvestcraft;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.addons.core.DefaultCropHandler;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class HarvestcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new HarvestcraftPlugin();

    static Class<?> BlockPamCrop;
    static Method BlockPamCrop_getCropItem;

    private HarvestcraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("mods.PamHarvestCraft.PamHarvestCraft");
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
            BlockPamCrop = Class.forName("mods.PamHarvestCraft.BlockPamCrop");
            BlockPamCrop_getCropItem = BlockPamCrop.getMethod("getCropItem");

            registrar.registerStackProvider(HUDHandlerPamCrops.INSTANCE, BlockPamCrop);

            registrar.registerHeadProvider(HUDHandlerPamCrops.INSTANCE, BlockPamCrop);

            registrar.registerCropHandler(new DefaultCropHandler(7), BlockPamCrop);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[PamHarvestCraft] Error while loading crop hooks.", t);
        }
    }

}
