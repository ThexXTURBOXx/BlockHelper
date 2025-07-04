package mcp.mobius.waila.api;

import java.util.Map;
import java.util.Set;

/**
 * Interface for Waila internal config storage.</br>
 * An instance of this interface is passed to most of Waila callbacks as a way to change the behavior depending on
 * client settings.
 */
public interface IPluginConfig {

    /**
     * Returns a set of all the currently loaded modules in the config handler.
     *
     * @return The module names
     */
    Set<String> getModuleNames();

    /**
     * Returns all the currently available options for a given module
     *
     * @param modName Module name
     * @return The config options
     */
    Map<String, String> getKeys(String modName);

    /**
     * Returns the current value of an option (true/false) with a default value if not set.
     *
     * @param key      Option to lookup
     * @param defvalue Default values
     * @return Value of the option or defvalue if not set.
     */
    boolean get(String key, boolean defvalue);

    /**
     * Returns the current value of an option (true/false) with a default value true if not set
     *
     * @param key Option to lookup
     * @return Value of the option or true if not set.
     */
    boolean get(String key);

    /**
     * Sets the value of an option (true/false). Does nothing on forced configs.
     * !WARNING! BE CAREFUL WITH THIS AS THIS CHANGES THE ACTUAL CONFIG!
     * Do not use invasively. A user should still be able to change stuff according
     * to their preferences. Thus, e.g., do not use this on every startup or whatever.
     * Otherwise, users will seriously dislike what you are doing...
     *
     * @param key   Option to lookup
     * @param value New value for the option
     * @return true if the value is now definitely set correctly. If false, then
     *         we are on the client and the config is either forced or it is a
     *         synced config and we do not know whether it is forced (yet)
     */
    boolean set(String key, boolean value);

}
