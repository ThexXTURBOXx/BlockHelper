package de.thexxturboxx.blockhelper.api;

/**
 * Provides functionality for fixing incorrect item names.
 */
public interface BlockHelperNameFixer {

    /**
     * Inspects the given {@link BlockHelperBlockState} and returns the correct item name.
     *
     * @param state The current {@link BlockHelperBlockState} of the block to fix.
     * @return The fixed item name or {@code null} if this class isn't supposed to fix it.
     */
    String getName(BlockHelperBlockState state);

}
