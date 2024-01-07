package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
        BlockHelperModSupport.registerNameFixer(new VanillaIntegration());
        BlockHelperModSupport.registerItemStackFixer(new VanillaIntegration());
    }

}
