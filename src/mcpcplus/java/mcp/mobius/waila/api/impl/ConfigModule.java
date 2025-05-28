package mcp.mobius.waila.api.impl;

import java.util.Set;
import java.util.TreeSet;

public class ConfigModule {

    final String modName;
    final Set<String> options;

    public ConfigModule(String modName) {
        this(modName, new TreeSet<String>());
    }

    public ConfigModule(String modName, Set<String> options) {
        this.modName = modName;
        this.options = options;
    }

    public void addOption(String key) {
        this.options.add(key);
    }

}
