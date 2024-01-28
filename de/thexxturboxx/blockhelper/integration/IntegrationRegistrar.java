package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerBlockProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerBlockProvider(new AppEngIntegration());
        BlockHelperModSupport.registerBlockProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerModFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerItemStackFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerBlockProvider(new CChunksIntegration());
        BlockHelperModSupport.registerModFixer(new CChunksIntegration());
        BlockHelperModSupport.registerBlockProvider(new EEIntegration());
        BlockHelperModSupport.registerItemStackFixer(new FactorizationIntegration());
        BlockHelperModSupport.registerModFixer(new FactorizationIntegration());
        BlockHelperModSupport.registerBlockProvider(new ForestryIntegration());
        BlockHelperModSupport.registerBlockProvider(new ForgeIntegration());
        BlockHelperModSupport.registerItemStackFixer(new GregTechIntegration());
        BlockHelperModSupport.registerBlockProvider(new Ic2Integration());
        BlockHelperModSupport.registerItemStackFixer(new Ic2Integration());
        BlockHelperModSupport.registerModFixer(new ImmibisIntegration());
        BlockHelperModSupport.registerBlockProvider(new MeteorsIntegration());
        BlockHelperModSupport.registerItemStackFixer(new MeteorsIntegration());
        BlockHelperModSupport.registerBlockProvider(new NaturaIntegration());
        BlockHelperModSupport.registerItemStackFixer(new NaturaIntegration());
        BlockHelperModSupport.registerItemStackFixer(new PamIntegration());
        BlockHelperModSupport.registerBlockProvider(new ProjectZuluIntegration());
        BlockHelperModSupport.registerItemStackFixer(new ProjectZuluIntegration());
        BlockHelperModSupport.registerItemStackFixer(new TEIntegration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
        BlockHelperModSupport.registerNameFixer(new VanillaIntegration());
        BlockHelperModSupport.registerItemStackFixer(new VanillaIntegration());
    }

}
