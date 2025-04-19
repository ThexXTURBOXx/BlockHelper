package mcp.mobius.waila.gui.screens.info;

import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.WAlign;
import mcp.mobius.waila.gui.screens.ScreenBase;
import mcp.mobius.waila.gui.widgets.ItemStackDisplay;
import mcp.mobius.waila.gui.widgets.LabelFixedFont;
import mcp.mobius.waila.gui.widgets.LayoutBase;
import mcp.mobius.waila.gui.widgets.ViewTable;
import mcp.mobius.waila.gui.widgets.WidgetGeometry;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public class ScreenEnchants extends ScreenBase {

    public ScreenEnchants(GuiScreen parent) {
        super(parent);

        this.getRoot().addWidget("Layout_Title", new LayoutBase(null))
                .setGeometry(new WidgetGeometry(50.0, 10.0, 50.0, 32.0, CType.REL_X, CType.REL_X,
                        WAlign.CENTER, WAlign.TOP));

        this.getRoot().getWidget("Layout_Title").addWidget("ItemStack", new ItemStackDisplay(null))
                .setGeometry(new WidgetGeometry(0.0, 0.0, 32.0, 32.0, CType.ABSXY, CType.ABSXY,
                        WAlign.LEFT, WAlign.TOP));

        this.getRoot().getWidget("Layout_Title").addWidget("LabelName",
                        new LabelFixedFont(null, I18n.translate("hud.msg.none")))
                .setGeometry(new WidgetGeometry(40.0, 4.0, 16.0, 16.0, CType.ABSXY, CType.ABSXY,
                        WAlign.LEFT, WAlign.TOP));

        this.getRoot().getWidget("Layout_Title").addWidget("LabelEnchantability",
                        new LabelFixedFont(null, I18n.translate("hud.msg.none")))
                .setGeometry(new WidgetGeometry(40.0, 22.0, 16.0, 16.0, CType.ABSXY, CType.REL_X,
                        WAlign.LEFT, WAlign.BOTTOM));

        this.getRoot().addWidget("Table", new ViewTable(null))
                .setGeometry(new WidgetGeometry(50.0, 50.0, 90.0, 80.0, CType.REL_X, CType.RELXY,
                        WAlign.CENTER, WAlign.TOP));

        String columnName = GREEN + ITALIC + I18n.translate("enchant.title.name");
        String columnMinLvl = GREEN + ITALIC + I18n.translate("enchant.title.minlvl");
        String columnMaxLvl = GREEN + ITALIC + I18n.translate("enchant.title.maxlvl");
        String columnWeight = GREEN + ITALIC + I18n.translate("enchant.title.weight");

        ((ViewTable) this.getRoot().getWidget("Table"))
                .setColumnsTitle(columnName, columnMinLvl, columnMaxLvl, columnWeight, GREEN + ITALIC + "Mod")
                .setColumnsWidth(35.0, 10.0, 10.0, 10.0, 35.0)
                .setColumnsAlign(WAlign.LEFT, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.LEFT);
    }

    public ViewTable getTable() {
        return (ViewTable) this.getRoot().getWidget("Table");
    }

    public ScreenEnchants addRow(String... strings) {
        this.getTable().addRow(strings);
        return this;
    }

    public ScreenEnchants setStack(ItemStack stack) {
        ((ItemStackDisplay) this.getRoot().getWidget("Layout_Title").getWidget("ItemStack")).setStack(stack);
        this.getRoot().getWidget("Layout_Title").adjustSize();
        return this;
    }

    public ScreenEnchants setName(String name) {
        ((LabelFixedFont) this.getRoot().getWidget("Layout_Title").getWidget("LabelName")).setText(name);
        this.getRoot().getWidget("Layout_Title").adjustSize();
        return this;
    }

    public ScreenEnchants setEnchantability(String value) {
        ((LabelFixedFont) this.getRoot().getWidget("Layout_Title").getWidget("LabelEnchantability"))
                .setText(I18n.translate("enchant.label.enchantability") + " : " + value);
        this.getRoot().getWidget("Layout_Title").adjustSize();
        return this;
    }

}
