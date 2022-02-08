package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;

public class ImmibisIntegration extends BlockHelperInfoProvider {

    @Override
    public String getMod(Object object) {
        if (iof(object, "mods.immibis.microblocks.ItemSaw")) {
            return "Immibis's Microblocks";
        }
        return super.getMod(object);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.immibisIntegration;
    }

}
