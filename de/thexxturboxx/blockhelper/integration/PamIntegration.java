package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.item.ItemStack;

public class PamIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "mods.PamHarvestCraft.BlockPamCrop")
                || iof(state.block, "mods.PamWeeeFlowers.BlockPamFlowerCrop")
                || iof(state.block, "mods.PamHarvestCraft.BlockPamRegrowCrop")) {
            ItemStack stack = state.block.getPickBlock(state.mop, state.world,
                    state.mop.blockX, state.mop.blockY, state.mop.blockZ);
            return stack != null ? stack
                    : new ItemStack(state.block.idDropped(7, null, 0), 1, state.block.damageDropped(0));
        }

        // Yes, this is more than just ugly... But Pam's code here is more than just ugly as well...
        Class<?> clazz = state.block.getClass();
        if (clazz.getName().matches("mods\\.PamHarvestCraft\\..*\\.BlockPam\\w*Crop")) {
            return new ItemStack(state.block.idDropped(7, null, 7), 1,
                    state.block.damageDropped(7));
        }
        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.pamIntegration;
    }

}
