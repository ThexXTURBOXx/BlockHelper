package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.interfaces.IWidget;

public class ButtonIntegerConfig extends ButtonInteger {

    private final String category;
    private final String configKey;
    private final boolean instant;

    public ButtonIntegerConfig(IWidget parent, String category, String configKey, String... texts) {
        this(parent, category, configKey, true, 0, texts);
    }

    public ButtonIntegerConfig(IWidget parent, String category, String configKey, boolean instant, int state_,
                               String... texts) {
        super(parent, texts);
        this.category = category;
        this.configKey = configKey;
        this.instant = instant;

        this.state = PluginConfig.instance().get(this.category, this.configKey, state_);

        for (int i = 0; i < this.nStates; i++)
            this.getWidget("Label_" + i).hide();

        this.getWidget("Label_" + state).show();
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        super.onMouseClick(event);

        if (this.instant)
            PluginConfig.instance().setConfig(this.category, this.configKey, this.state);
    }

}
