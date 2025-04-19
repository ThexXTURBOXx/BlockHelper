package mcp.mobius.waila.proxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import mcp.mobius.waila.addons.nei.NEIHandler;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ModLoader;

public class ProxyClient extends ProxyCommon {

    public ProxyClient() {
        super(Side.CLIENT);
    }

    @Override
    public void prepare() {
        super.prepare();

        ModLoader.setInGameHook(mod_BlockHelper.INSTANCE, true, false);

        I18n.INSTANCE.addLangDirFromHost(ProxyClient.class, "/assets/waila/lang");
        LanguageRegistry.reloadLanguageTable();

        if (Loader.isModLoaded("NotEnoughItems")) {
            try {
                NEIHandler.register();
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
    }

}
