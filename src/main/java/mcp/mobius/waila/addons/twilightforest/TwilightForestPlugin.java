package mcp.mobius.waila.addons.twilightforest;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public class TwilightForestPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new TwilightForestPlugin();

    static Class<?> BlockTFRoots;
    static Class<?> BlockTFPlant;
    static Class<?> BlockTFSapling;

    private TwilightForestPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("twilightforest.TwilightForestMod");
            mod_BlockHelper.LOG.log(Level.INFO, "[TwilightForestMod] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[TwilightForestMod] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            BlockTFRoots = AccessHelper.getClass("twilightforest.block.BlockTFRoots");
            BlockTFPlant = AccessHelper.getClass("twilightforest.block.BlockTFPlant");
            BlockTFSapling = AccessHelper.getClass("twilightforest.block.BlockTFSapling");

            if (side.isClient()) {
                registrar.registerStackProvider(HUDTwilightForestGenericOverride.INSTANCE, BlockTFRoots);
                registrar.registerStackProvider(HUDTwilightForestGenericOverride.INSTANCE, BlockTFPlant);
                registrar.registerStackProvider(HUDTwilightForestGenericOverride.INSTANCE, BlockTFSapling);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[TwilightForestMod] Error while loading item hooks.", t);
        }
    }

}
