package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import net.meteor.common.MeteorsMod;
import net.minecraft.item.ItemStack;

public class MeteorsIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "net.meteor.common.block.BlockMeteorShieldTorch")) {
            info.add(I18n.format(state.translator, "state_format", I18n.format(state.translator,
                    state.id == MeteorsMod.torchMeteorShieldActive.blockID ? "protected" : "unprotected")));
        }
        if (iof(state.te, "net.meteor.common.tileentity.TileEntityMeteorShield")) {
            if (state.meta == 0) {
                info.add(I18n.format(state.translator, "state_format",
                        I18n.format(state.translator, "charging")));
            } else {
                String size = state.meta * 4 + "x" + state.meta * 4;
                info.add(I18n.format(state.translator, "radius_format",
                        I18n.format(state.translator, "chunks_format", size)));
            }
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "net.meteor.common.block.BlockMeteorShieldTorch")) {
            return new ItemStack(state.block.idDropped(0, null, 0), 1, state.meta);
        }
        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.meteorsIntegration;
    }

}
