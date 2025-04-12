package mcp.mobius.waila.overlay.tooltiprenderers;

import org.lwjgl.util.Dimension;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.IconUI;

import static mcp.mobius.waila.api.SpecialChars.WailaIcon;
import static mcp.mobius.waila.api.SpecialChars.WailaStyle;

public class TTRenderIcon implements ITooltipRenderer {

    final String type;
    final int IconSize = 8;

    public TTRenderIcon(String type) {
        this.type = type;
    }

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(IconSize, IconSize);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        DisplayUtil.renderIcon(x, y, IconSize, IconSize, IconUI.bySymbol(WailaStyle + WailaIcon + type));
    }

}
