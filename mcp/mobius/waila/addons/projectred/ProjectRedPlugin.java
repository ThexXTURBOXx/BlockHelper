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
            Class.forName("mrtjp.projectred.ProjectRed");
            mod_BlockHelper.LOG.log(Level.INFO, "ProjectRed mod found.");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ProjectRed] ProjectRed mod not found.");
            return;
        }

        registrar.addConfigRemote("Project:Red", "pr.showio");
        registrar.addConfigRemote("Project:Red", "pr.showdata");
        registrar.addConfigRemote("Project:Red", "pr.showsignal");

        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_sgate");
        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_igate");
        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_tgate");
        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_bgate");
        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_agate");
        registrar.registerBodyProvider(HUDFMPGateLogic.INSTANCE, "pr_rgate");
        registrar.registerBodyProvider(HUDFMPWires.INSTANCE, "pr_redwire");
        registrar.registerBodyProvider(HUDFMPWires.INSTANCE, "pr_insulated");
        registrar.registerBodyProvider(HUDFMPWires.INSTANCE, "pr_fredwire");
        registrar.registerBodyProvider(HUDFMPWires.INSTANCE, "pr_finsulated");

        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_sgate");
        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_igate");
        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_tgate");
        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_bgate");
        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_agate");
        registrar.registerDecorator(HUDDecoratorRsGateLogic.INSTANCE, "pr_rgate");

        //registrar.registerBlockDecorator(HUDDecoratorRsGateLogic.INSTANCE, BlockMultipart);
        //registrar.registerBodyProvider(HUDHandlerRsGateLogic.INSTANCE, BlockMultipart);
        //registrar.registerSyncedNBTKey("*", BlockMultipart);
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
