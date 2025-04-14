package mcp.mobius.waila.addons.projectred;

import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class ProjectRedPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ProjectRedPlugin();

    private ProjectRedPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("mrtjp.projectred.ProjectRedIntegration");
            mod_BlockHelper.LOG.log(Level.INFO, "ProjectRed|Integration mod found.");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ProjectRed] ProjectRed|Integration mod not found.");
            return;
        }

        registrar.addConfigRemote("Project:Red", "pr.showio");
        registrar.addConfigRemote("Project:Red", "pr.showdata");

        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_sgate");
        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_igate");
        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_tgate");
        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_bgate");
        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_agate");
        registrar.registerBodyProvider(new HUDFMPGateLogic(), "pr_rgate");

        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_sgate");
        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_igate");
        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_tgate");
        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_bgate");
        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_agate");
        registrar.registerDecorator(new HUDDecoratorRsGateLogic(), "pr_rgate");

        //registrar.registerBlockDecorator(new HUDDecoratorRsGateLogic(), BlockMultipart);
        //registrar.registerBodyProvider(new HUDHandlerRsGateLogic(), BlockMultipart);
        //registrar.registerSyncedNBTKey("*", BlockMultipart);
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
