package mcp.mobius.waila.addons.forge;

import java.util.logging.Level;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.liquids.ITankContainer;

public class ForgeModule {

    public static void register() {
        try {
            WailaRegistrar.instance().addConfig("Forge", "forge.tankamount");
            WailaRegistrar.instance().addConfig("Forge", "forge.tanktype");
            WailaRegistrar.instance().registerHeadProvider(new HUDHandlerForgeTanks(), ITankContainer.class);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerForgeTanks(), ITankContainer.class);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Forge] Error while loading Tank hooks.", t);
        }

    }

}
