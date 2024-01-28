package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import ee.EEMaps;
import ee3.emc.EMCList;
import ee3.emc.EMCValue;
import ee3.mod_EE3;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.ItemStack;

public class EEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        try {
            int emc = EEMaps.getEMC(new ItemStack(state.id, 1, state.meta));
            if (emc > 0) {
                info.add(I18n.format(state.translator, "emc", emc));
            }
        } catch (Throwable ignored) {
        }

        try {
            Map<Integer, HashMap<Integer, EMCValue>> emcMap = getDeclaredField(EMCList.class, mod_EE3.emcList,
                    "emcMap");
            if (emcMap != null) {
                Map<Integer, EMCValue> metaMap = emcMap.get(state.id);
                if (metaMap != null) {
                    EMCValue value = metaMap.get(state.meta);
                    if (value != null) {
                        info.add(I18n.format(state.translator, "emc", value.getCostEMC()));
                    }
                }
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.eeIntegration;
    }

}
