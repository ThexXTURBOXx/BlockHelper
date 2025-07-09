package mcp.mobius.waila.gui.screens.config;

import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.WAlign;
import mcp.mobius.waila.gui.screens.ScreenBase;
import mcp.mobius.waila.gui.widgets.LayoutBase;
import mcp.mobius.waila.gui.widgets.WidgetGeometry;
import mcp.mobius.waila.gui.widgets.buttons.ButtonBooleanConfig;
import mcp.mobius.waila.gui.widgets.buttons.ButtonContainerLabel;
import mcp.mobius.waila.gui.widgets.buttons.ButtonScreenChange;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.src.GuiScreen;
import net.minecraftforge.common.Configuration;

public class ScreenWailaConfig extends ScreenBase {

    public ScreenWailaConfig(GuiScreen parent) {
        super(parent);

        this.getRoot().addWidget("ButtonContainer", new ButtonContainerLabel(this.getRoot(), 2, 100, 25.0));
        this.getRoot().getWidget("ButtonContainer").setGeometry(new WidgetGeometry(0.0, 10.0, 100.0, 50.0,
                CType.RELXY, CType.RELXY));

        ButtonContainerLabel buttonContainer = ((ButtonContainerLabel) this.getRoot().getWidget("ButtonContainer"));

        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHOW, true, "screen.button.hidden", "screen.button.visible"),
                "choice.showhidewaila");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_MODE, true, "screen.button.maintained", "screen.button.toggled"),
                "choice.toggledmaintained");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_METADATA, true, "screen.button.hidden", "screen.button.visible"),
                "choice.showhideidmeta");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_LIQUID, false, "screen.button.hidden", "screen.button.visible"),
                "choice.showliquids");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHIFTBLOCK, false, "screen.button.no", "screen.button.yes"),
                "choice.shifttoggledblock");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHIFTENTS, false, "screen.button.no", "screen.button.yes"),
                "choice.shifttoggledents");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHOWICON, true, "screen.button.hidden", "screen.button.visible"),
                "choice.showicon");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_UPDATE_CHECK, true, "screen.button.no", "screen.button.yes"),
                "choice.updatecheck");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_FIXER_NOTIFY, true, "screen.button.no", "screen.button.yes"),
                "choice.fixernotify");
        buttonContainer.addButton(new ButtonBooleanConfig(this.getRoot(), Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_HIDE_IN_DEBUG, true, "screen.button.no", "screen.button.yes"),
                "choice.hideindebug");

        this.getRoot().addWidget("LayoutConfigPos", new LayoutBase(this.getRoot()));
        this.getRoot().getWidget("LayoutConfigPos").setGeometry(
                new WidgetGeometry(0.0, 60.0, 100.0, 20.0, CType.RELXY, CType.RELXY));
        this.getRoot().getWidget("LayoutConfigPos").addWidget("ButtonConfigPos",
                new ButtonScreenChange(null, "screen.button.configureaspect", new ScreenHUDConfig(this)));
        this.getRoot().getWidget("LayoutConfigPos").getWidget("ButtonConfigPos").setGeometry(
                new WidgetGeometry(50.0, 50.0, 150.0, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));

        this.getRoot().addWidget("LayoutBack", new LayoutBase(this.getRoot()));
        this.getRoot().getWidget("LayoutBack").setGeometry(
                new WidgetGeometry(0.0, 80.0, 100.0, 20.0, CType.RELXY, CType.RELXY));
        this.getRoot().getWidget("LayoutBack").addWidget("ButtonBack", new ButtonScreenChange(
                null, "screen.button.back", this.parent));
        this.getRoot().getWidget("LayoutBack").getWidget("ButtonBack").setGeometry(
                new WidgetGeometry(50.0, 50.0, 100.0, 20.0, CType.RELXY, CType.ABSXY, WAlign.CENTER, WAlign.CENTER));
    }

}
