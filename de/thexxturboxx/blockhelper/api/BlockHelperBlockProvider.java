package de.thexxturboxx.blockhelper.api;

/**
 * Provides functionality for adding additional information about a block.
 */
public interface BlockHelperBlockProvider extends Switchable {

    /**
     * Adds information about a given block in its current {@link BlockHelperBlockState} to the given
     * {@link InfoHolder} object.
     *
     * @param state The current {@link BlockHelperBlockState} of the block to inspect.
     * @param info  The {@link InfoHolder} to add the information to.
     */
    void addInformation(BlockHelperBlockState state, InfoHolder info);

}
