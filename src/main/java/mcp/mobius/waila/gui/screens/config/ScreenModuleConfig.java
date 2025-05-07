package mcp.mobius.waila.gui.screens.config;

import java.util.Map;
import java.util.Set;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.WAlign;
import mcp.mobius.waila.gui.screens.ScreenBase;
import mcp.mobius.waila.gui.widgets.LayoutBase;
import mcp.mobius.waila.gui.widgets.ViewportScrollable;
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

        this.getRoot().addWidget("Viewport", new ViewportScrollable(null))
                .setGeometry(new WidgetGeometry(0.0, 20.0, 100.0, 60.0, CType.RELXY, CType.RELXY,
                        WAlign.LEFT, WAlign.TOP));
        IWidget holder = new LayoutBase(null);
        ((ViewportScrollable) (this.getRoot().getWidget("Viewport"))).attachWidget(holder)
                .setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 0.0, CType.RELXY, CType.REL_X,
                        WAlign.LEFT, WAlign.TOP));

        int columns = 2;
        double spacing = 25.0;
        holder.addWidget("ButtonContainer", new ButtonContainerLabel(holder, columns, 100, spacing));
        holder.getWidget("ButtonContainer").setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0,
                CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));

        ButtonContainerLabel buttonContainer = ((ButtonContainerLabel) holder.getWidget("ButtonContainer"));

        Set<Map.Entry<String, String>> entries = PluginConfig.instance().getKeys(modName).entrySet();
        for (Map.Entry<String, String> e : entries) {
            String key = e.getKey();
            if (PluginConfig.instance().isSyncedConfig(key))
                buttonContainer.addButton(new ButtonBooleanSyncedConfig(holder, Constants.CATEGORY_MODULES, key,
                        "screen.button.no", "screen.button.yes"), e.getValue());
            else
                buttonContainer.addButton(new ButtonBooleanConfig(holder, Constants.CATEGORY_MODULES, key,
                        "screen.button.no", "screen.button.yes"), e.getValue());
        }

        int rows = (entries.size() + (columns - 1)) / columns; // = ceilDiv
        holder.setSize(100.0, spacing * rows);

        this.getRoot().addWidget("LayoutBack", new LayoutBase(this.getRoot()));
        this.getRoot().getWidget("LayoutBack").setGeometry(new WidgetGeometry(0.0, 80.0, 100.0, 20.0,
                CType.RELXY, CType.RELXY));
        this.getRoot().getWidget("LayoutBack").addWidget("ButtonBack",
                new ButtonScreenChange(this.getRoot().getWidget("LayoutBack"), "screen.button.back", this.parent));
        this.getRoot().getWidget("LayoutBack").getWidget("ButtonBack").setGeometry(new WidgetGeometry(50.0, 50.0,
                100.0, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));
    }

}
