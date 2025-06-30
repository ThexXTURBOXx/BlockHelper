package mcp.mobius.waila.api.event;

import mcp.mobius.waila.api.IWailaPlugin;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

public class WailaRegisterEvent extends Event {

    @Cancelable
    public static class Plugin extends WailaRegisterEvent {

        private final IWailaPlugin plugin;

        public Plugin(IWailaPlugin plugin) {
            this.plugin = plugin;
        }

        public IWailaPlugin getPlugin() {
            return this.plugin;
        }

    }

    public static class Config extends WailaRegisterEvent {

        private final String module;
        private final String key;
        private final String translationKey;
        private boolean defValue;
        private final boolean synced;

        public Config(String module, String key, String translationKey, boolean defValue,
                      boolean synced) {
            this.module = module;
            this.key = key;
            this.translationKey = translationKey;
            this.defValue = defValue;
            this.synced = synced;
        }

        public String getModule() {
            return this.module;
        }

        public String getKey() {
            return this.key;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }

        public boolean getDefaultValue() {
            return this.defValue;
        }

        public void setDefaultValue(boolean defValue) {
            this.defValue = defValue;
        }

        public boolean isSynced() {
            return this.synced;
        }

    }

}
