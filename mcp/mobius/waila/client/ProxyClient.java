package mcp.mobius.waila.client;

import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.mod_Waila;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.gui.truetyper.FontLoader;
import mcp.mobius.waila.gui.truetyper.TrueTypeFont;
import mcp.mobius.waila.handlers.HUDHandlerBlocks;
import mcp.mobius.waila.handlers.HUDHandlerEntities;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderHealth;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderProgressBar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;
//import mcp.mobius.waila.handlers.SummaryProviderDefault;
import mcp.mobius.waila.server.ProxyServer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import cpw.mods.fml.common.Loader;

public class ProxyClient extends ProxyServer {

	TrueTypeFont minecraftiaFont;

	//public static LangUtil lang = LangUtil.loadLangDir("waila");

	public ProxyClient() {}


	@Override
	public void registerHandlers(){
		super.registerHandlers();

		LangUtil.instance.addLangDirFromJar(LangUtil.instance.hostFile(ProxyClient.class), "/assets/waila/lang");

		minecraftiaFont = FontLoader.createFont("/assets/waila/fonts/Minecraftia.ttf", 14, true);

		//TickRegistry.registerTickHandler(WailaTickHandler.instance(), Side.CLIENT);

		if (Loader.isModLoaded("NotEnoughItems")){
			try{
				Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("register").invoke(null);
			} catch (Exception e){
				mod_Waila.log.severe("Failed to hook into NEI properly. Mod names not shown in item tooltips.");
			}
		}

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerBlocks(), Block.class);
		ModuleRegistrar.instance().registerTailProvider(new HUDHandlerBlocks(), Block.class);

		ModuleRegistrar.instance().registerHeadProvider(new HUDHandlerEntities(), Entity.class);
		ModuleRegistrar.instance().registerTailProvider(new HUDHandlerEntities(), Entity.class);

		//ModuleRegistrar.instance().registerShortDataProvider(new SummaryProviderDefault(), Item.class);

		ModuleRegistrar.instance().addConfig("General", "general.showents");
		ModuleRegistrar.instance().addConfig("General", "general.showcrop");

		ModuleRegistrar.instance().registerTooltipRenderer("waila.health",    new TTRenderHealth());
		ModuleRegistrar.instance().registerTooltipRenderer("waila.stack",     new TTRenderStack());
		ModuleRegistrar.instance().registerTooltipRenderer("waila.progress",  new TTRenderProgressBar());
	}

	@Override
	public Object getFont(){return this.minecraftiaFont;}

}
