package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import ee.EEMaps;
import ee3.common.emc.EMCEntry;
import ee3.common.emc.EMCRegistry;
import net.minecraft.item.ItemStack;

public class EEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        try {
            EMCEntry entry = EMCRegistry.instance().getEMCValue(state.id, state.meta);
            if (entry != null) {
                info.add(I18n.format(state.translator, "emc", entry.getCost()));
            }
        } catch (Throwable ignored) {
        }

        try {
            int emc = EEMaps.getEMC(new ItemStack(state.id, 1, state.meta));
            if (emc > 0) {
                info.add(I18n.format(state.translator, "emc", emc));
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.eeIntegration;
    }

}