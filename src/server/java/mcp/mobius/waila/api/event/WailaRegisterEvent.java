package mcp.mobius.waila.api.event;

import mcp.mobius.waila.api.IWailaPlugin;

public class WailaRegisterEvent {

    public static class Plugin extends WailaRegisterEvent {

        private final IWailaPlugin plugin;
        private boolean cancelled = false;

        public Plugin(IWailaPlugin plugin) {
            this.plugin = plugin;
        }

        public IWailaPlugin getPlugin() {
            return this.plugin;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public boolean isCancelled() {
            return cancelled;
        }

    }

    public static class Config extends WailaRegisterEvent {

        private final String module;
        private final String key;
        private boolean defValue;

        public Config(String module, String key, boolean defValue) {
            this.module = module;
            this.key = key;
            this.defValue = defValue;
        }

        public String getModule() {
            return this.module;
        }

        public String getKey() {
            return this.key;
        }

        public boolean getDefaultValue() {
            return this.defValue;
        }

        public void setDefaultValue(boolean defValue) {
            this.defValue = defValue;
        }

    }

}
