package mcp.mobius.waila.api.impl;

import java.util.HashMap;
import java.util.Map;

public class ConfigModule {

    final String modName;
    final Map<String, String> options;

    public ConfigModule(String _modName) {
        this.modName = _modName;
        this.options = new HashMap<String, String>();
    }

    public ConfigModule(String _modName, Map<String, String> _options) {
        this.modName = _modName;
        this.options = _options;
    }

    public void addOption(String key, String name) {
        this.options.put(key, name);
    }

}
