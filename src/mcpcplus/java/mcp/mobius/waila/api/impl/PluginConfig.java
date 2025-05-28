package mcp.mobius.waila.api.impl;

import forge.Configuration;
import forge.Property;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.utils.Constants;

public class PluginConfig implements IPluginConfig {

    /* SINGLETON */
    private static PluginConfig _instance = null;

    private PluginConfig() {
        _instance = this;
    }

    public static PluginConfig instance() {
        return _instance == null ? new PluginConfig() : _instance;
    }
    /* === */

    private final Map<String, ConfigModule> modules = new LinkedHashMap<String, ConfigModule>();
    public Configuration config = null;

    public ConfigModule addModule(String modName) {
        return this.addModule(modName, new ConfigModule(modName));
    }

    public ConfigModule addModule(String modName, ConfigModule options) {
        this.modules.put(modName, options);
        return options;
    }

    @Override
    public Set<String> getModuleNames() {
        return this.modules.keySet();
    }

    @Override
    public Set<String> getKeys(String modName) {
        return this.modules.containsKey(modName) ? this.modules.get(modName).options : new HashSet<String>();
    }

    public void addSyncedConfig(String modName, String key) {
        this.addSyncedConfig(modName, key, Constants.CFG_DEFAULT_VALUE);
    }

    public void addSyncedConfig(String modName, String key, boolean defValue) {
        this.config.getOrCreateBooleanProperty(key, Constants.CATEGORY_MODULES, defValue);
        this.config.getOrCreateBooleanProperty(key, Constants.CATEGORY_SERVER, Constants.SERVER_FREE);
        this.config.save();

        if (!this.modules.containsKey(modName))
            this.addModule(modName);

        this.modules.get(modName).addOption(key);
    }

    @Override
    public boolean get(String key) {
        return this.get(key, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public boolean get(String key, boolean defValue) {
        Property prop = this.config.getOrCreateBooleanProperty(key, Constants.CATEGORY_MODULES, defValue);
        return prop.getBoolean(defValue);
    }






    /* GENERAL ACCESS METHODS TO GET/SET VALUES IN THE CONFIG FILE */

    public boolean get(String category, String key, boolean default_) {
        Property prop = this.config.getOrCreateBooleanProperty(key, category, default_);
        return prop.getBoolean(default_);
    }

    public void setConfig(String category, String key, boolean state) {
        this.config.getOrCreateBooleanProperty(key, category, state).value = String.valueOf(state);
        this.config.save();
    }

    public int get(String category, String key, int default_) {
        Property prop = this.config.getOrCreateIntProperty(key, category, default_);
        return prop.getInt();
    }

    public void setConfig(String category, String key, int state) {
        this.config.getOrCreateIntProperty(key, category, state).value = String.valueOf(state);
        this.config.save();
    }



    /* Default config loading */

    public void loadDefaultConfig(Configuration cfg) {
        this.config = cfg;
        this.config.load();
        this.config.save();
    }

}
