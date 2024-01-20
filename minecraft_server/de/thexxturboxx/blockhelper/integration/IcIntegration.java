package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;

public class IcIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        Integer chargePoints = getField(state.te.getClass(), state.te, "chargePoints");
        if (chargePoints != null) {
            Integer chargened = getField(state.te.getClass(), state.te, "chargened");
            String maxCharge = "";
            if (chargened == null)
                chargened = getDeclaredField(state.te.getClass(), state.te, "chargened");
            if (chargened != null)
                maxCharge = " / " + chargened + " EU";
            info.add(chargePoints + " EU" + maxCharge);
        }

        if (iof(state.te, "TileEntityTransmit") || iof(state.te, "TileEntityTransformer")
            || iof(state.te, "TileEntityTesla")) {
            Integer furnaceBurnTime = getDeclaredField(state.te.getClass(), state.te, "furnaceBurnTime");
            Integer powerStorage = getField(state.te.getClass(), state.te, "powerStorage");
            if (furnaceBurnTime != null && powerStorage != null)
                info.add(furnaceBurnTime + " EU / " + powerStorage + " EU");
        }

        if (iof(state.te, "TileEntityMatterGen")) {
            Integer matterGeneration = getField(state.te.getClass(), state.te, "matterGeneration");
            Integer matterCost = getDeclaredField(state.te.getClass(), state.te, "matterCost");
            if (matterGeneration != null && matterCost != null) {
                int p = (int) (100f * matterGeneration / matterCost);
                info.add(I18n.format("progress_format", Math.min(p, 100) + "%"));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.icIntegration;
    }

}
