package mcp.mobius.waila.api.impl;

import java.util.Map;
import java.util.TreeMap;

public class ConfigModule {

    final String modName;
    final Map<String, String> options;

    public ConfigModule(String modName) {
        this(modName, new TreeMap<String, String>());
    }

    public ConfigModule(String modName, Map<String, String> options) {
        this.modName = modName;
        this.options = options;
    }

    public void addOption(String key, String translationKey) {
        this.options.put(key, translationKey);
    }

}
