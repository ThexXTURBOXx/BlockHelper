package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerBlockProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerBlockProvider(new Ic2Integration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
    }

}