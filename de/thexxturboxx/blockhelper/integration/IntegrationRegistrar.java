package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerBlockProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerBlockProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerModFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerItemStackFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerBlockProvider(new ForestryIntegration());
        BlockHelperModSupport.registerItemStackFixer(new GregTechIntegration());
        BlockHelperModSupport.registerBlockProvider(new Ic2Integration());
        BlockHelperModSupport.registerItemStackFixer(new Ic2Integration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
    }

}
