package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerTileEntityProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerBlockProvider(new FSIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new Ic2Integration());
        BlockHelperModSupport.registerBlockProvider(new RedPower2Integration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
    }

}
