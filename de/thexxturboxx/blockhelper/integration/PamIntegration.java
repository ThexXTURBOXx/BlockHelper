package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.item.ItemStack;

public class PamIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        // Yes, this is more than just ugly... But Pam's code here is more than just ugly as well...
        Class<?> clazz = state.block.getClass();
        if (clazz.getName().matches("pamsmods\\.common\\.weeeflowers\\.BlockPam\\w*FlowerCrop")) {
            return state.block
                    .getBlockDropped(state.world, state.mop.blockX, state.mop.blockY, state.mop.blockZ, 7, -10)
                    .get(0);
        }
        return super.getItemStack(state);
    }

}
