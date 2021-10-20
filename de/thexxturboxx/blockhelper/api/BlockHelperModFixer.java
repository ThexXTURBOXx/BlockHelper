package de.thexxturboxx.blockhelper.api;

/**
 * Provides functionality for fixing incorrect mod names.
 */
public interface BlockHelperModFixer {

    /**
     * Inspects the given {@link BlockHelperBlockState} and returns the correct mod name.
     *
     * @param state The current {@link BlockHelperBlockState} of the block to fix.
     * @return The fixed mod name or {@code null} if this class isn't supposed to fix it.
     */
    String getMod(BlockHelperBlockState state);

    /**
     * Inspects the given object and returns the correct mod name. The object can be anything, but is most likely
     * an instance of {@link net.minecraft.item.Item}.
     *
     * @param object The object to fix.
     * @return The fixed mod name or {@code null} if this class isn't supposed to fix it.
     */
    String getMod(Object object);

}
