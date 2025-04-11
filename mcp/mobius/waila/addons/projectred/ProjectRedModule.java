package mcp.mobius.waila.addons.projectred;

import java.util.logging.Level;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;

public class ProjectRedModule {

    public static void register() {
        try {
            Class<?> ModClass = Class.forName("mrtjp.projectred.ProjectRedIntegration");
            mod_BlockHelper.LOG.log(Level.INFO, "ProjectRed|Integration mod found.");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ProjectRed] ProjectRed|Integration mod not found.");
            return;
        }

        WailaRegistrar.instance().addConfigRemote("Project:Red", "pr.showio");
        WailaRegistrar.instance().addConfigRemote("Project:Red", "pr.showdata");

        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_sgate");
        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_igate");
        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_tgate");
        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_bgate");
        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_agate");
        WailaRegistrar.instance().registerBodyProvider(new HUDFMPGateLogic(), "pr_rgate");

        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_sgate");
        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_igate");
        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_tgate");
        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_bgate");
        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_agate");
        WailaRegistrar.instance().registerDecorator(new HUDDecoratorRsGateLogic(), "pr_rgate");

        //ModuleRegistrar.instance().registerBlockDecorator(new HUDDecoratorRsGateLogic(), BlockMultipart);
        //ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerRsGateLogic(), BlockMultipart);
        //ModuleRegistrar.instance().registerSyncedNBTKey("*", BlockMultipart);
    }

}
