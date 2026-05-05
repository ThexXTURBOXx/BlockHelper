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
import mcp.mobius.waila.api.event.WailaRegisterEvent;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.FixDetector;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
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

    public String liquidUnit;

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
    public Map<String, String> getKeys(String modName) {
        return this.modules.containsKey(modName) ? this.modules.get(modName).options : new HashMap<String, String>();
    }

    public void addConfig(String modName, String key, String translationKey) {
        this.addConfig(modName, key, translationKey, Constants.CFG_DEFAULT_VALUE);
    }

    public void addConfig(String modName, String key, String translationKey, boolean defValue) {
        this.addConfigInternal(modName, key, translationKey, defValue, false);
    }

    public void addSyncedConfig(String modName, String key, String translationKey) {
        this.addSyncedConfig(modName, key, translationKey, Constants.CFG_DEFAULT_VALUE);
    }

    public void addSyncedConfig(String modName, String key, String translationKey, boolean defValue) {
        this.config.get(Constants.CATEGORY_SERVER, key, Constants.SERVER_FREE);
        this.addConfigInternal(modName, key, translationKey, defValue, true);
        this.syncedConfigs.add(key);
    }

    private void addConfigInternal(String modName, String key, String translationKey, boolean defValue,
                                   boolean synced) {
        WailaRegisterEvent.Config event = new WailaRegisterEvent.Config(modName, key, translationKey, defValue, synced);
        MinecraftForge.EVENT_BUS.post(event);

        this.config.get(Constants.CATEGORY_MODULES, key, event.getDefaultValue());
        this.config.save();

        if (!this.modules.containsKey(modName))
            this.addModule(modName);

        this.modules.get(modName).addOption(key, translationKey);
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

        Property prop = this.config.get(Constants.CATEGORY_MODULES, key, defvalue);
        return prop.getBoolean(defvalue);
    }

    @Override
    public boolean set(String key, boolean value) {
        if (this.syncedConfigs.contains(key) && !mod_BlockHelper.INSTANCE.serverPresent
            && !FMLCommonHandler.instance().getEffectiveSide().isServer())
            return false;

        if (mod_BlockHelper.INSTANCE.serverPresent && this.forcedConfigs.containsKey(key))
            return false;

        Property prop = this.config.get(Constants.CATEGORY_MODULES, key, value);
        prop.value = Boolean.toString(value);

        this.config.save();
        return true;
    }

    public boolean isSyncedConfig(String key) {
        return this.syncedConfigs.contains(key);
    }






    /* GENERAL ACCESS METHODS TO GET/SET VALUES IN THE CONFIG FILE */

    public boolean get(String category, String key, boolean default_) {
        Property prop = this.config.get(category, key, default_);
        return prop.getBoolean(default_);
    }

    public void setConfig(String category, String key, boolean state) {
        String value = String.valueOf(state);
        this.config.get(category, key, value, Property.Type.BOOLEAN).value = value;
        this.config.save();
    }

    public int get(String category, String key, int default_) {
        Property prop = this.config.get(category, key, default_);
        return prop.getInt();
    }

    public void setConfig(String category, String key, int state) {
        String value = String.valueOf(state);
        this.config.get(category, key, value, Property.Type.INTEGER).value = value;
        this.config.save();
    }

    public String get(String category, String key, String default_) {
        Property prop = this.config.get(category, key, default_);
        return prop.value;
    }

    public void setConfig(String category, String key, String state) {
        this.config.get(category, key, state, Property.Type.STRING).value = state;
        this.config.save();
    }


    /* Some accessor helpers */
    public boolean showTooltip() {
        return get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
    }

    public boolean showIcon() {
        return get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOWICON, true);
    }



    /* Default config loading */

    public void loadDefaultConfig(Configuration cfg) {
        this.config = cfg;
        this.config.load();

        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, false);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_KEYBIND, true);
        get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOWICON, true);
        liquidUnit = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUIDUNIT, "mB");

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
        int iconAlign = get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_ICON_ALIGN, 1);
        if (iconAlign < 0 || iconAlign > 2)
            setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_ICON_ALIGN, 1);

        this.config.addCustomCategoryComment(Constants.CATEGORY_MODULES,
                "Those are the config keys defined in modules.\n" +
                "Server side, it is used to enforce keys client side using the next section.");
        this.config.addCustomCategoryComment(Constants.CATEGORY_SERVER,
                "Any key set to true here will ensure that the client is using the configuration set in the 'module' " +
                "section above.\n" +
                "This is useful for enforcing false to 'cheating' keys like silverfish.");

        this.config.save();
    }

}
