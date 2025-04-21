package mcp.mobius.waila.gui.screens.config;

import java.util.Set;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.WAlign;
import mcp.mobius.waila.gui.screens.ScreenBase;
import mcp.mobius.waila.gui.widgets.LayoutBase;
import mcp.mobius.waila.gui.widgets.ViewportScrollable;
import mcp.mobius.waila.gui.widgets.WidgetGeometry;
import mcp.mobius.waila.gui.widgets.buttons.ButtonContainer;
import mcp.mobius.waila.gui.widgets.buttons.ButtonScreenChange;
import net.minecraft.client.gui.GuiScreen;

public class ScreenModuleChoice extends ScreenBase {

    public ScreenModuleChoice(GuiScreen parent) {
        super(parent);

        this.getRoot().addWidget("Viewport", new ViewportScrollable(null))
                .setGeometry(new WidgetGeometry(50.0, 20.0, 400.0, 60.0, CType.RELXY, CType.REL_Y,
                        WAlign.CENTER, WAlign.TOP));
        IWidget holder = new LayoutBase(null);
        ((ViewportScrollable) (this.getRoot().getWidget("Viewport"))).attachWidget(holder)
                .setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 0.0, CType.RELXY, CType.REL_X,
                        WAlign.LEFT, WAlign.TOP));

        int columns = 3;
        double spacing = 25.0;
        holder.addWidget("ButtonContainer", new ButtonContainer(holder, columns, 100, spacing));
        holder.getWidget("ButtonContainer").setGeometry(new WidgetGeometry(0.0, 0.0, 100.0, 100.0,
                CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));

        ButtonContainer buttonContainer = ((ButtonContainer) holder.getWidget("ButtonContainer"));

        Set<String> keys = PluginConfig.instance().getModuleNames();
        for (String key : keys)
            buttonContainer.addButton(new ButtonScreenChange(holder, key, new ScreenModuleConfig(this, key)));

        int rows = (keys.size() + (columns - 1)) / columns; // = ceilDiv
        holder.setSize(100.0, spacing * rows);

        this.getRoot().addWidget("LayoutBack", new LayoutBase(this.getRoot()));
        this.getRoot().getWidget("LayoutBack").setGeometry(new WidgetGeometry(0.0, 80.0, 100.0, 20.0, CType.RELXY,
                CType.RELXY));
        this.getRoot().getWidget("LayoutBack").addWidget("ButtonBack",
                new ButtonScreenChange(this.getRoot().getWidget("LayoutBack"), "Back", this.parent));
        this.getRoot().getWidget("LayoutBack").getWidget("ButtonBack").setGeometry(new WidgetGeometry(50.0, 50.0,
                100.0, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));
    }

}
