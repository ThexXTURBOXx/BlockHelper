package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import mods.immibis.am2.TileAM2Base;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "mods.immibis.am2.TileAM2Base")) {
            TileAM2Base tam = (TileAM2Base) state.te;
            info.add(this.<Integer>getDeclaredField(tam, "storedEnergy") + " EU / "
                    + this.<Integer>getDeclaredField(TileAM2Base.class, "MAX_STORAGE") + " EU");
        }
    }

}
