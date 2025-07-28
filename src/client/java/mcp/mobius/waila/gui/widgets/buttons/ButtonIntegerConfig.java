package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.interfaces.IWidget;

public class ButtonIntegerConfig extends ButtonInteger {

    private final String category;
    private final String configKey;

    public ButtonIntegerConfig(IWidget parent, String category, String configKey, String... texts) {
        this(parent, category, configKey, 0, texts);
    }

    public ButtonIntegerConfig(IWidget parent, String category, String configKey, int state_, String... texts) {
        super(parent, texts);
        this.category = category;
        this.configKey = configKey;

        this.state = PluginConfig.instance().get(this.category, this.configKey, state_);

        if (this.state < 0)
            this.state = 0;

        if (this.state >= this.nStates)
            this.state = 0;

        for (int i = 0; i < this.nStates; i++)
            this.getWidget("Label_" + i).hide();

        this.getWidget("Label_" + state).show();
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        super.onMouseClick(event);
        PluginConfig.instance().setConfig(this.category, this.configKey, this.state);
    }

}
