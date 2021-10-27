package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerBlockProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerBlockProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerItemStackFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerModFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerBlockProvider(new ForestryIntegration());
        BlockHelperModSupport.registerBlockProvider(new FSIntegration());
        BlockHelperModSupport.registerBlockProvider(new Ic2Integration());
        BlockHelperModSupport.registerItemStackFixer(new Ic2Integration());
        BlockHelperModSupport.registerItemStackFixer(new PamIntegration());
        BlockHelperModSupport.registerBlockProvider(new RedPower2Integration());
        BlockHelperModSupport.registerItemStackFixer(new RedPower2Integration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
        BlockHelperModSupport.registerNameFixer(new VanillaIntegration());
    }

}
