package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import mods.immibis.am2.TileAM2Base;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "mods.immibis.am2.TileAM2Base")) {
            TileAM2Base tam = (TileAM2Base) state.te;
            Integer stored = getDeclaredField(TileAM2Base.class, tam, "storedEnergy");
            Integer max = getDeclaredField(TileAM2Base.class, null, "MAX_STORAGE");
            if (max != null && max != 0) {
                info.add(stored + " EU / " + max + " EU");
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.advMachinesIntegration;
    }

}
