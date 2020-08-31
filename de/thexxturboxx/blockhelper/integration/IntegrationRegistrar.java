package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public class IntegrationRegistrar {

    public static void init() {
        BlockHelperModSupport.registerInfoProvider(new AppEngIntegration());
        BlockHelperModSupport.registerInfoProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerInfoProvider(new CChunksIntegration());
        BlockHelperModSupport.registerInfoProvider(new FSIntegration());
        BlockHelperModSupport.registerInfoProvider(new Ic2Integration());
        BlockHelperModSupport.registerInfoProvider(new MeteorsIntegration());
        BlockHelperModSupport.registerInfoProvider(new TEIntegration());
        BlockHelperModSupport.registerInfoProvider(new VanillaIntegration());
    }

}
