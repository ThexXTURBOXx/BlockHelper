package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import ee3.common.emc.EMCEntry;
import ee3.common.emc.EMCRegistry;

public class EEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        EMCEntry entry = EMCRegistry.instance().getEMCValue(state.id, state.meta);
        if (entry != null) {
            info.add(I18n.format(state.translator, "emc", entry.getCost()));
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.eeIntegration;
    }

}
