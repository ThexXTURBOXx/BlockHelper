package mcp.mobius.waila.api.impl;

import java.util.HashMap;
import java.util.Map;

public class ConfigModule {

    final String modName;
    final Map<String, String> options;

    public ConfigModule(String modName) {
        this(modName, new HashMap<String, String>());
    }

    public ConfigModule(String modName, Map<String, String> options) {
        this.modName = modName;
        this.options = options;
    }

    public void addOption(String key, String name) {
        this.options.put(key, name);
    }

}
