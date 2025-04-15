package mcp.mobius.waila.addons.forge;

import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.liquids.ITankContainer;

public final class ForgePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ForgePlugin();

    private ForgePlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            registrar.addConfig("Forge", "forge.tankamount");
            registrar.addConfig("Forge", "forge.tanktype");

            registrar.registerHeadProvider(HUDHandlerForgeTanks.INSTANCE, ITankContainer.class);
            registrar.registerBodyProvider(HUDHandlerForgeTanks.INSTANCE, ITankContainer.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Forge] Error while loading Tank hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
