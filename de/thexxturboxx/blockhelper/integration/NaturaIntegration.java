package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class NaturaIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "mods.natura.blocks.crops.CropBlock")) {
            return state.block.getPickBlock(state.mop, state.world,
                    state.mop.blockX, state.mop.blockY, state.mop.blockZ);
        }
        return super.getItemStack(state);
    }

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "mods.natura.blocks.crops.BerryBush")
                || iof(state.block, "mods.natura.blocks.crops.NetherBerryBush")) {
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
        return BlockHelperCommonProxy.naturaIntegration;
    }

}
