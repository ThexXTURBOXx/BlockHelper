package mcp.mobius.waila.api.impl;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mcp.mobius.waila.addons.vanilla.HUDHandlerEntities;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

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
    private final List<String> syncedConfigs = new ArrayList<String>();
    public Map<String, Boolean> forcedConfigs = new HashMap<String, Boolean>();
    public Configuration config = null;

    public void addModule(String modName, Map<String, String> options) {
        this.addModule(modName, new ConfigModule(modName, options));
    }

    public void addModule(String modName, ConfigModule options) {
        this.modules.put(modName, options);
    }

    @Override
    public Set<String> getModuleNames() {
        return this.modules.keySet();
    }

    @Override
    public Map<String, String> getKeys(String modName) {
        return this.modules.containsKey(modName) ? this.modules.get(modName).options : null;
    }

    private void saveModuleKey(String modName, String key) {
        this.saveModuleKey(modName, key, Constants.CFG_DEFAULT_VALUE);
    }

    private void saveModuleKey(String modName, String key, boolean defvalue) {
        config.get(Constants.CATEGORY_MODULES, key, defvalue);
        config.get(Constants.CATEGORY_SERVER, key, Constants.SERVER_FREE);
        config.save();
    }

    public void addConfig(String modName, String key, String name) {
        this.addConfig(modName, key, name, Constants.CFG_DEFAULT_VALUE);
    }

    public void addConfig(String modName, String key, String name, boolean defvalue) {
        this.saveModuleKey(modName, key, defvalue);

        if (!this.modules.containsKey(modName))
            this.modules.put(modName, new ConfigModule(modName));

        this.modules.get(modName).addOption(key, name);
    }

    public void addSyncedConfig(String modName, String key, String name) {
        this.addSyncedConfig(modName, key, name, Constants.CFG_DEFAULT_VALUE);
    }

    public void addSyncedConfig(String modName, String key, String name, boolean defvalue) {
        this.saveModuleKey(modName, key, defvalue);

        if (!this.modules.containsKey(modName))
            this.modules.put(modName, new ConfigModule(modName));

        this.modules.get(modName).addOption(key, name);
        this.syncedConfigs.add(key);
    }

    @Override
    public boolean get(String key) {
        return this.get(key, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public boolean get(String key, boolean defvalue) {
        if (this.syncedConfigs.contains(key) && !mod_BlockHelper.INSTANCE.serverPresent
            && !FMLCommonHandler.instance().getEffectiveSide().isServer())
            return false;

        if (mod_BlockHelper.INSTANCE.serverPresent && this.forcedConfigs.containsKey(key))
            return this.forcedConfigs.get(key);

        Property prop = config.get(Constants.CATEGORY_MODULES, key, defvalue);
        return prop.getBoolean(defvalue);
    }

    public boolean isSyncedConfig(String key) {
        return this.syncedConfigs.contains(key);
    }






    /* GENERAL ACCESS METHODS TO GET/SET VALUES IN THE CONFIG FILE */

    public boolean get(String category, String key, boolean default_) {
        Property prop = config.get(category, key, default_);
        return prop.getBoolean(default_);
    }

    public void setConfig(String category, String key, boolean state) {
        config.getCategory(category).put(key, new Property(key, String.valueOf(state), Property.Type.BOOLEAN));
        config.save();
    }

    public int get(String category, String key, int default_) {
        Property prop = config.get(category, key, default_);
        return prop.getInt();
    }

    public void setConfig(String category, String key, int state) {
        config.getCategory(category).put(key, new Property(key, String.valueOf(state), Property.Type.INTEGER));
        config.save();
    }


    /* Some accessor helpers */
    public boolean showTooltip() {
        return get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
    }



    /* Default config loading */

    public void loadDefaultConfig(Configuration cfg) {
        config = cfg;
        config.load();

        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, false);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_KEYBIND, true);

        OverlayConfig.posX = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSX, 5000);
        OverlayConfig.posY = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 100);
        OverlayConfig.scale = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SCALE, 100) / 100.0f;

        OverlayConfig.alpha = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_ALPHA, 80);
        OverlayConfig.bgcolor = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_BGCOLOR, 0x100010);
        OverlayConfig.gradient1 = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_GRADIENT1, 0x5000ff);
        OverlayConfig.gradient2 = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_GRADIENT2, 0x28007f);
        OverlayConfig.fontcolor = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_FONTCOLOR, 0xA0A0A0);

        HUDHandlerEntities.nhearts = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_NHEARTS, 20);
        HUDHandlerEntities.maxhpfortext = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MAXHP, 40);

        mod_BlockHelper.UPDATER.notify = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_UPDATE_CHECK, true);
        FixDetector.notify = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_FIXER_NOTIFY, true);
        mod_BlockHelper.DEV_MODE = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_DEV_MODE, false);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_HIDE_IN_DEBUG, true);

        config.getCategory(Constants.CATEGORY_MODULES).setComment(
                "Those are the config keys defined in modules.\n" +
                "Server side, it is used to enforce keys client side using the next section.");
        config.getCategory(Constants.CATEGORY_SERVER).setComment(
                "Any key set to true here will ensure that the client is using the configuration set in the 'module' " +
                "section above.\n" +
                "This is useful for enforcing false to 'cheating' keys like silverfish.");

        config.save();
    }

}
