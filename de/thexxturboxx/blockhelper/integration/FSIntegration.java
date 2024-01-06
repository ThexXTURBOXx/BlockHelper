package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import net.minecraft.util.MathHelper;

public class FSIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "florasoma.berries.BerryBush")) {
            int newMeta = MathHelper.floor_double(state.meta / 4d);
            if (newMeta < 3) {
                int grow = (int) ((newMeta / 2d) * 100);
                String toShow;
                if (grow >= 100) {
                    toShow = I18n.format(state.translator, "mature");
                } else {
                    toShow = grow + "%";
                }
                info.add(I18n.format(state.translator, "growth_state_format", toShow));
            } else {
                info.add(I18n.format(state.translator, "growth_state_format", I18n.format(state.translator, "ripe")));
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.floraSomaIntegration;
    }

}
