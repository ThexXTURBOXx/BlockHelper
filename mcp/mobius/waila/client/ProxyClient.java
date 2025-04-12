package mcp.mobius.waila.client;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.LangUtil;
import mcp.mobius.waila.handlers.HUDHandlerBlocks;
import mcp.mobius.waila.handlers.HUDHandlerDev;
import mcp.mobius.waila.handlers.HUDHandlerEntities;
import mcp.mobius.waila.handlers.HUDHandlerEntitiesDev;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
import mcp.mobius.waila.server.ProxyServer;
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

        //TickRegistry.registerTickHandler(WailaTickHandler.instance(), Side.CLIENT);

        if (Loader.isModLoaded("NotEnoughItems")) {
            try {
                Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("register").invoke(null);
            } catch (Throwable t) {
                mod_BlockHelper.LOG.severe("Failed to hook into NEI properly. Mod names not shown in item tooltips.");
            }
        }

        WailaRegistrar.instance().registerHeadProvider(new HUDHandlerBlocks(), Block.class);
        WailaRegistrar.instance().registerTailProvider(new HUDHandlerBlocks(), Block.class);

        WailaRegistrar.instance().registerHeadProvider(new HUDHandlerEntities(), Entity.class);
        WailaRegistrar.instance().registerTailProvider(new HUDHandlerEntities(), Entity.class);
        WailaRegistrar.instance().registerStackProvider(new HUDHandlerEntities(), Entity.class);

        //ModuleRegistrar.instance().registerShortDataProvider(new SummaryProviderDefault(), Item.class);

        WailaRegistrar.instance().addConfig("General", "general.showents");
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
