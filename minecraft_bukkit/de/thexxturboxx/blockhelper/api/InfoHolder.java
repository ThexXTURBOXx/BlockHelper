package de.thexxturboxx.blockhelper.api;

/**
 * Contains and manages information about the current state of BlockHelper.
 */
public interface InfoHolder {

    /**
     * Adds information to this {@link InfoHolder} object.
     *
     * @param data The information to add.
     * @return The current {@link InfoHolder} object for chain-calls.
     */
    InfoHolder add(String data);

}
