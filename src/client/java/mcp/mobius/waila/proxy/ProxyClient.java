package mcp.mobius.waila.proxy;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;

public class ProxyClient extends ProxyCommon {

    public ProxyClient() {
        super();
    }

    @Override
    public void prepare() {
        super.prepare();

        ModLoader.SetInGameHook(mod_BlockHelper.INSTANCE, true, false);
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

}
