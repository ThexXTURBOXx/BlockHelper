package de.thexxturboxx.blockhelper.api;

import net.minecraft.item.ItemStack;

/**
 * Provides functionality for fixing incorrect {@link ItemStack}s based on a block's current state.
 */
public interface BlockHelperItemStackFixer {

    /**
     * Inspects the given {@link BlockHelperBlockState} and returns a correct {@link ItemStack} that represents the
     * block best.
     *
     * @param state The current {@link BlockHelperBlockState} of the block to fix.
     * @return The fixed {@link ItemStack} or {@code null} if this class isn't supposed to fix it.
     */
    ItemStack getItemStack(BlockHelperBlockState state);

}
