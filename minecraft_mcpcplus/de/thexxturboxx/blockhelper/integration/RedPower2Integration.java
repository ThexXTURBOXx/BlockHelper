package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;

public class RedPower2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "eloraam.world.BlockCustomCrops")) {
            if (state.meta < 5) {
                int grow = (int) ((state.meta / 4d) * 100);
                String toShow;
                if (grow >= 100) {
                    toShow = I18n.format("mature");
                } else {
                    toShow = grow + "%";
                }
                info.add(I18n.format("growth_state_format", toShow));
            } else {
                info.add(I18n.format("growth_state_format", I18n.format("ripe")));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.redPower2Integration;
    }

}
