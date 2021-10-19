package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
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
        if (iof(state.block, "mods.natura.blocks.crops.BerryBush")) {
            int newMeta = MathHelper.floor_double(state.meta / 4d);
            if (newMeta < 3) {
                int grow = (int) ((newMeta / 2d) * 100);
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
