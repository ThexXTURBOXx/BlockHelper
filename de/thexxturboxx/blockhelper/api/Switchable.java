package de.thexxturboxx.blockhelper.api;

/**
 * Allows certain functionality to be enabled or disabled.
 */
public interface Switchable {

    /**
     * Determines whether the current functionality shall be enabled.
     *
     * @return Whether the current functionality shall be enabled.
     */
    boolean isEnabled();

}
