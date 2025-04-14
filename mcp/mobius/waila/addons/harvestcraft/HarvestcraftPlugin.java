package mcp.mobius.waila.addons.harvestcraft;

import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class HarvestcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new HarvestcraftPlugin();

    public static Class<?> TileEntityPamCrop = null;

    private HarvestcraftPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("assets.pamharvestcraft.PamHarvestCraft");
            mod_BlockHelper.LOG.log(Level.INFO, "PamHarvestCraft mod found.");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.INFO, "[PamHarvestCraft] PamHarvestCraft mod not found.");
            return;
        }

        try {
            TileEntityPamCrop = Class.forName("assets.pamharvestcraft.TileEntityPamCrop");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[PamHarvestCraft] Class not found. ", e);
            return;
        }

        registrar.registerBodyProvider(new HUDHandlerPamCrop(), TileEntityPamCrop);
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
