package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerInfoProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerInfoProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerInfoProvider(new Ic2Integration());
        BlockHelperModSupport.registerInfoProvider(new VanillaIntegration());
    }

}
