package mcp.mobius.waila.addons.harvestcraft;

import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;

public class HarvestcraftModule {

    public static Class<?> TileEntityPamCrop = null;

    public static void register() {
        try {
            Class<?> PamHarvestCraft = Class.forName("assets.pamharvestcraft.PamHarvestCraft");
            mod_BlockHelper.log.log(Level.INFO, "PamHarvestCraft mod found.");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.log.log(Level.INFO, "[PamHarvestCraft] PamHarvestCraft mod not found.");
            return;
        }

        try {
            TileEntityPamCrop = Class.forName("assets.pamharvestcraft.TileEntityPamCrop");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.log.log(Level.WARNING, "[PamHarvestCraft] Class not found. " + e);
            return;
        }

        ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerPamCrop(), TileEntityPamCrop);
    }
}
