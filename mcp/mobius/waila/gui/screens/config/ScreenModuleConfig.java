package mcp.mobius.waila.gui.screens.config;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.WAlign;
import mcp.mobius.waila.gui.screens.ScreenBase;
import mcp.mobius.waila.gui.widgets.LayoutBase;
import mcp.mobius.waila.gui.widgets.WidgetGeometry;
import mcp.mobius.waila.gui.widgets.buttons.ButtonBooleanConfig;
import mcp.mobius.waila.gui.widgets.buttons.ButtonBooleanSyncedConfig;
import mcp.mobius.waila.gui.widgets.buttons.ButtonContainerLabel;
import mcp.mobius.waila.gui.widgets.buttons.ButtonScreenChange;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.gui.GuiScreen;

public class ScreenModuleConfig extends ScreenBase {

    public ScreenModuleConfig(GuiScreen parent, String modName) {
        super(parent);

        this.getRoot().addWidget("ButtonContainer", new ButtonContainerLabel(this.getRoot(), 2, 100, 25.0));
        this.getRoot().getWidget("ButtonContainer").setGeometry(new WidgetGeometry(0.0, 20.0, 100.0, 60.0,
                CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));

        ButtonContainerLabel buttonContainer = ((ButtonContainerLabel) this.getRoot().getWidget("ButtonContainer"));

        for (String key : PluginConfig.instance().getKeys(modName).keySet()) {
            if (PluginConfig.instance().isServerRequired(key))
                buttonContainer.addButton(new ButtonBooleanSyncedConfig(this.getRoot(), Constants.CATEGORY_MODULES, key,
                                "screen.button.no", "screen.button.yes"),
                        PluginConfig.instance().getKeys(modName).get(key));
            else
                buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Constants.CATEGORY_MODULES, key,
                                "screen.button.no", "screen.button.yes"),
                        PluginConfig.instance().getKeys(modName).get(key));
        }

        this.getRoot().addWidget("LayoutBack", new LayoutBase(this.getRoot()));
        this.getRoot().getWidget("LayoutBack").setGeometry(new WidgetGeometry(0.0, 80.0, 100.0, 20.0,
                CType.RELXY, CType.RELXY));
        this.getRoot().getWidget("LayoutBack").addWidget("ButtonBack",
                new ButtonScreenChange(this.getRoot().getWidget("LayoutBack"), "screen.button.back", this.parent));
        this.getRoot().getWidget("LayoutBack").getWidget("ButtonBack").setGeometry(new WidgetGeometry(50.0, 50.0,
                100.0, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));
    }

}
