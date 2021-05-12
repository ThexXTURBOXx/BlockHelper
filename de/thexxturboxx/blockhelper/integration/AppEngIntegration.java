package de.thexxturboxx.blockhelper.integration;

import appeng.me.basetiles.TilePoweredBase;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.api.InfoHolder;

public class AppEngIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperState state, InfoHolder info) {
        if (iof(state.te, "appeng.me.basetiles.TilePoweredBase")) {
            info.add(((TilePoweredBase) state.te).storedPower + " AE / "
                    + ((TilePoweredBase) state.te).maxStoredPower + " AE");
        }
    }

}
