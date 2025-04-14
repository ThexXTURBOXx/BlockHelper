package mcp.mobius.waila.client;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;
import mcp.mobius.waila.server.ProxyServer;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.src.ModLoader;

public class ProxyClient extends ProxyServer {

    public ProxyClient() {
    }

    @Override
    public void prepare() {
        super.prepare();

        ModLoader.setInGameHook(mod_BlockHelper.INSTANCE, true, false);

        LangUtil.INSTANCE.addLangDirFromJar(LangUtil.INSTANCE.hostFile(ProxyClient.class), "/assets/waila/lang");

        if (Loader.isModLoaded("NotEnoughItems")) {
            try {
                Class.forName("mcp.mobius.waila.addons.nei.NEIHandler").getDeclaredMethod("register").invoke(null);
            } catch (Throwable t) {
                mod_BlockHelper.LOG.severe("Failed to hook into NEI properly. Mod names not shown in item tooltips.");
            }
        }
    }

    @Override
    public void registerCorePlugins(IRegistrar registrar) {
        super.registerCorePlugins(registrar);

        registrar.registerTooltipRenderer("waila.health", new TTRenderHealth());
        registrar.registerTooltipRenderer("waila.icon", new TTRenderIcon());
        registrar.registerTooltipRenderer("waila.progress", new TTRenderProgressBar());
        registrar.registerTooltipRenderer("waila.stack", new TTRenderStack());
        registrar.registerTooltipRenderer("waila.string", new TTRenderString());

        CorePlugin.INSTANCE.registerClient(registrar);
    }

    @Override
    public void registerModPlugins(IRegistrar registrar) {
        super.registerModPlugins(registrar);

        for (IWailaPlugin plugin : plugins)
            plugin.registerClient(registrar);
    }

}
