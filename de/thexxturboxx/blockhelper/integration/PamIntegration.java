package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.item.ItemStack;

public class PamIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "mods.PamHarvestCraft.BlockPamCrop")
                || iof(state.block, "mods.PamWeeeFlowers.BlockPamFlowerCrop")
                || iof(state.block, "mods.PamHarvestCraft.BlockPamRegrowCrop")) {
            return state.block.getPickBlock(state.mop, state.world,
                    state.mop.blockX, state.mop.blockY, state.mop.blockZ);
        }
        return super.getItemStack(state);
    }

}
