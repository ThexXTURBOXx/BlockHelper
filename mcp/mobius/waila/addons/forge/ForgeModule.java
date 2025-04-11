package mcp.mobius.waila.addons.forge;

import java.util.logging.Level;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.liquids.ITankContainer;

public class ForgeModule {

    public static void register() {
        try {
            ModuleRegistrar.instance().addConfig("Forge", "forge.tankamount");
            ModuleRegistrar.instance().addConfig("Forge", "forge.tanktype");
            ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerForgeTanks(), ITankContainer.class);
            ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerForgeTanks(), ITankContainer.class);
        } catch (Exception e) {
            mod_BlockHelper.log.log(Level.WARNING, "[Forge] Error while loading Tank hooks." + e);
        }

    }

}
