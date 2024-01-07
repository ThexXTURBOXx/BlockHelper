package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import lightningrod.TileEntityLightningrod;

public class GregTechIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "lightningrod.TileEntityLightningrod")) {
            try {
                TileEntityLightningrod telr = (TileEntityLightningrod) state.te;
                Class<?> modLightningrod = getClass("mod_Lightningrod");
                Object instance = getMethod(modLightningrod, "getInstance").invoke(null);
                Integer maxEnergy = getField(modLightningrod, instance, "mLightningStrikeEnergy");
                if (maxEnergy != null && maxEnergy != 0) {
                    info.add(telr.mStoredEnergy + " EU / " + maxEnergy + " EU");
                }
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.gregTechIntegration;
    }

}