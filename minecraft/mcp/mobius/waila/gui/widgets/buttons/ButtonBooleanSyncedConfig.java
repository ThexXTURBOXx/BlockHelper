package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.GLState;
import org.lwjgl.util.Point;

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
    public void onMouseClick(MouseEvent event) {
        if (!isForcedConfig())
            super.onMouseClick(event);
    }

    @Override
    public void draw(Point pos) {
        if (isForcedConfig()) {
            GLState state = new GLState();
            int texOffset = -1;
            this.mc.renderEngine.bindTexture(WIDGETS_TEXTURE);
            UIHelper.drawTexture(this.getPos().getX(), this.getPos().getY(), this.getSize().getX(),
                    this.getSize().getY(), 0, 66 + texOffset * 20, 200, 20);
            state.reset();
        } else {
            super.draw(pos);
        }
    }

    public boolean isForcedConfig() {
        return mod_BlockHelper.INSTANCE.serverPresent && PluginConfig.instance().forcedConfigs.containsKey(this.configKey);
    }

}
