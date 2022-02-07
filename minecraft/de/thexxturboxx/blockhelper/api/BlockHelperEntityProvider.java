package de.thexxturboxx.blockhelper.api;

/**
 * Provides functionality for adding additional information about an entity.
 */
public interface BlockHelperEntityProvider extends Switchable {

    /**
     * Adds information about a given entity in its current {@link BlockHelperEntityState} to the given
     * {@link InfoHolder} object.
     *
     * @param state The current {@link BlockHelperEntityState} of the block to inspect.
     * @param info  The {@link InfoHolder} to add the information to.
     */
    void addInformation(BlockHelperEntityState state, InfoHolder info);

}
