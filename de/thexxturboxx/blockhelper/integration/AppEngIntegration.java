package de.thexxturboxx.blockhelper.integration;

import appeng.me.basetiles.TilePoweredBase;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;

public class AppEngIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "appeng.me.basetiles.TilePoweredBase")) {
            TilePoweredBase tpb = (TilePoweredBase) state.te;
            if (tpb.maxStoredPower != 0) {
                info.add(tpb.storedPower + " AE / " + tpb.maxStoredPower + " AE");
            }
        }
    }

}
