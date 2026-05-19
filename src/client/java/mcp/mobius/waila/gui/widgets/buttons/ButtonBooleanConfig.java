package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.interfaces.IWidget;
import org.lwjgl.util.Point;

public class ButtonBooleanConfig extends ButtonBoolean {

    protected String category;
    protected String configKey;

    public ButtonBooleanConfig(IWidget parent, String category, String configKey, String textFalse, String textTrue) {
        this(parent, category, configKey, true, textFalse, textTrue);
    }

    public ButtonBooleanConfig(IWidget parent, String category, String configKey, boolean state_,
                               String textFalse, String textTrue) {
        super(parent, textFalse, textTrue);
        this.category = category;
        this.configKey = configKey;

        this.state = PluginConfig.instance().get(this.category, this.configKey, state_);

        if (this.state) {
            this.getWidget("LabelTrue").show();
            this.getWidget("LabelFalse").hide();
        } else {
            this.getWidget("LabelTrue").hide();
            this.getWidget("LabelFalse").show();
        }
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        if (!this.isForcedConfig()) {
            super.onMouseClick(event);
            PluginConfig.instance().setConfig(this.category, this.configKey, this.state);
        }
    }

    @Override
    public void draw(Point pos) {
        this.setEnabled(!this.isForcedConfig());
        super.draw(pos);
    }

    public boolean isForcedConfig() {
        return PluginConfig.instance().forcedConfigs.containsKey(this.configKey);
    }

}
