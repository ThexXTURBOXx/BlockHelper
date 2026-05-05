package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.mod_BlockHelper;

public class ButtonBooleanSyncedConfig extends ButtonBooleanConfig {

    public ButtonBooleanSyncedConfig(IWidget parent, String category, String configKey,
                                     String textFalse, String textTrue) {
        this(parent, category, configKey, true, textFalse, textTrue);
    }

    public ButtonBooleanSyncedConfig(IWidget parent, String category, String configKey, boolean state_,
                                     String textFalse, String textTrue) {
        super(parent, category, configKey, state_, textFalse, textTrue);

        if (this.isForcedConfig())
            this.state = PluginConfig.instance().forcedConfigs.get(this.configKey);

        if (this.state) {
            this.getWidget("LabelTrue").show();
            this.getWidget("LabelFalse").hide();
        } else {
            this.getWidget("LabelTrue").hide();
            this.getWidget("LabelFalse").show();
        }
    }

    @Override
    public boolean isForcedConfig() {
        return mod_BlockHelper.INSTANCE.serverPresent && super.isForcedConfig();
    }

}
