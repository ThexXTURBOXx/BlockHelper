package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperModSupport;

public final class IntegrationRegistrar {

    private IntegrationRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        BlockHelperModSupport.registerTileEntityProvider(new AdvMachinesIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new AppEngIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new BuildcraftIntegration());
        BlockHelperModSupport.registerModFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerItemStackFixer(new BuildcraftIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new CChunksIntegration());
        BlockHelperModSupport.registerModFixer(new CChunksIntegration());
        BlockHelperModSupport.registerItemStackFixer(new FactorizationIntegration());
        BlockHelperModSupport.registerModFixer(new FactorizationIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new ForgeIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new Ic2Integration());
        BlockHelperModSupport.registerItemStackFixer(new Ic2Integration());
        BlockHelperModSupport.registerBlockProvider(new MeteorsIntegration());
        BlockHelperModSupport.registerTileEntityProvider(new MeteorsIntegration());
        BlockHelperModSupport.registerItemStackFixer(new MeteorsIntegration());
        BlockHelperModSupport.registerBlockProvider(new NaturaIntegration());
        BlockHelperModSupport.registerItemStackFixer(new TEIntegration());
        BlockHelperModSupport.registerBlockProvider(new VanillaIntegration());
    }

}
