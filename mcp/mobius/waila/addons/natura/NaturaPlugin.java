package mcp.mobius.waila.addons.natura;

import java.util.logging.Level;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class NaturaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new NaturaPlugin();

    private NaturaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class.forName("mods.natura.Natura");
            mod_BlockHelper.LOG.log(Level.INFO, "[Natura] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Natura] Mod not found.");
        }
        return false;
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        try {
            Class<?> CropBlock = Class.forName("mods.natura.blocks.crops.CropBlock");
            VanillaPlugin.MAX_STAGES.put(CropBlock, 3);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Natura] Error while loading crop hooks.", t);
        }
    }

}
