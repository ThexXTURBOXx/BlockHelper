package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;

public class RedPower2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "eloraam.world.BlockCustomCrops")) {
            if (state.meta < 5) {
                int grow = (int) ((state.meta / 4d) * 100);
                String toShow;
                if (grow >= 100) {
                    toShow = "Mature";
                } else {
                    toShow = grow + "%";
                }
                info.add("Growth State: " + toShow);
            } else {
                info.add("Growth State: Ripe");
            }
        }
    }

}
