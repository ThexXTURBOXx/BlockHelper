package mcp.mobius.waila.client;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.addons.core.HUDHandlerBlocks;
import mcp.mobius.waila.addons.core.HUDHandlerDev;
import mcp.mobius.waila.addons.core.HUDHandlerEntities;
import mcp.mobius.waila.addons.core.HUDHandlerEntitiesDev;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.server.ProxyServer;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.src.ModLoader;

public class ProxyClient extends ProxyServer {

    public ProxyClient() {
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();

        ModLoader.setInGameHook(mod_BlockHelper.INSTANCE, true, false);

        LangUtil.INSTANCE.addLangDirFromJar(LangUtil.INSTANCE.hostFile(ProxyClient.class), "/assets/waila/lang");

        if (Loader.isModLoaded("NotEnoughItems")) {
            try {
                Class.forName("mcp.mobius.waila.addons.nei.NEIHandler").getDeclaredMethod("register").invoke(null);
            } catch (Throwable t) {
                mod_BlockHelper.LOG.severe("Failed to hook into NEI properly. Mod names not shown in item tooltips.");
            }
        }

        HUDHandlerBlocks.register();

        HUDHandlerEntities.register();

        WailaRegistrar.instance().addConfig("General", "general.showcrop");

        WailaRegistrar.instance().registerTooltipRenderer("waila.health", new TTRenderHealth());
        WailaRegistrar.instance().registerTooltipRenderer("waila.stack", new TTRenderStack());
        WailaRegistrar.instance().registerTooltipRenderer("waila.progress", new TTRenderProgressBar());

        if (mod_BlockHelper.DEV_MODE) {
            WailaRegistrar.instance().addConfig("General", "general.dev", false);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerDev(), Block.class);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerEntitiesDev(), Entity.class);
        }
    }

}
