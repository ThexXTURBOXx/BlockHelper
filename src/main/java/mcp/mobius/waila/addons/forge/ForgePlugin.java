package mcp.mobius.waila.addons.forge;

import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.BlockCauldron;
import net.minecraftforge.liquids.ITankContainer;

public final class ForgePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ForgePlugin();

    private ForgePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            registrar.addSyncedConfig("Forge", "forge.tankamount");
            registrar.addSyncedConfig("Forge", "forge.tanktype");

            if (side.isClient())
                registrar.addConfig("Forge", "forge.liquidbars");

            registrar.registerNBTProvider(HUDHandlerForgeTanks.INSTANCE, ITankContainer.class);
            registrar.registerNBTProvider(HUDHandlerEntityForgeTanks.INSTANCE, ITankContainer.class);

            if (side.isClient()) {
                registrar.registerHeadProvider(HUDHandlerForgeTanks.INSTANCE, ITankContainer.class);
                registrar.registerHeadProvider(HUDHandlerForgeTanks.INSTANCE, BlockCauldron.class);
                registrar.registerHeadProvider(HUDHandlerEntityForgeTanks.INSTANCE, ITankContainer.class);

                registrar.registerBodyProvider(HUDHandlerForgeTanks.INSTANCE, ITankContainer.class);
                registrar.registerBodyProvider(HUDHandlerForgeTanks.INSTANCE, BlockCauldron.class);
                registrar.registerBodyProvider(HUDHandlerEntityForgeTanks.INSTANCE, ITankContainer.class);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Forge] Error while loading Tank hooks.", t);
        }
    }

}
