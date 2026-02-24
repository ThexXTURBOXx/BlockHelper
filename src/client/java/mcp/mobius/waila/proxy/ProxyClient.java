package mcp.mobius.waila.proxy;

import java.util.logging.Level;
import mcp.mobius.waila.addons.apron.ApronHandler;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderEnergyBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
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
        ModLoader.SetInGUIHook(mod_BlockHelper.INSTANCE, true, false);
    }

    @Override
    public void registerCorePlugins(IRegistrar registrar) {
        super.registerCorePlugins(registrar);

        registrar.registerTooltipRenderer("waila.health", new TTRenderHealth());
        registrar.registerTooltipRenderer("waila.icon", new TTRenderIcon());
        registrar.registerTooltipRenderer("waila.progress", new TTRenderProgressBar());
        registrar.registerTooltipRenderer("waila.stack", new TTRenderStack());
        registrar.registerTooltipRenderer("waila.string", new TTRenderString());
        registrar.registerTooltipRenderer("waila.liquid", new TTRenderLiquidBar());
        registrar.registerTooltipRenderer("waila.energy", new TTRenderEnergyBar());
    }

    @Override
    public void postLoad() {
        super.postLoad();

        try {
            Class.forName("io.github.betterthanupdates.apron.stapi.blockhelper.TooltipRegistrar");
            try {
                ApronHandler.register();
                mod_BlockHelper.LOG.info("[Apron] Successfully registered Apron hooks!");
            } catch (Throwable t) {
                mod_BlockHelper.LOG.log(Level.WARNING,
                        "[Apron] Failed to hook into Apron properly. Mod names not shown in item tooltips.", t);
            }
        } catch (Throwable ignored) {
        }
    }

}
