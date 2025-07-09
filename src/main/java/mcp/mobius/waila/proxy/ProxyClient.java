package mcp.mobius.waila.proxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import java.util.logging.Level;
import mcp.mobius.waila.addons.nei.NEIHandler;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;

public class ProxyClient extends ProxyCommon {

    public ProxyClient() {
        super(Side.CLIENT);
    }

    @Override
    public void registerCorePlugins(IRegistrar registrar) {
        super.registerCorePlugins(registrar);

        registrar.registerTooltipRenderer("waila.health", new TTRenderHealth());
        registrar.registerTooltipRenderer("waila.icon", new TTRenderIcon());
        registrar.registerTooltipRenderer("waila.progress", new TTRenderProgressBar());
        registrar.registerTooltipRenderer("waila.stack", new TTRenderStack());
        registrar.registerTooltipRenderer("waila.string", new TTRenderString());
    }

    @Override
    public void postLoad() {
        super.postLoad();

        if (Loader.isModLoaded("NotEnoughItems")) {
            try {
                NEIHandler.register();
                mod_BlockHelper.LOG.info("[NEI] Successfully registered NEI hooks!");
            } catch (Throwable t) {
                mod_BlockHelper.LOG.log(Level.WARNING,
                        "[NEI] Failed to hook into NEI properly. Mod names not shown in item tooltips.", t);
            }
        }
    }

}
