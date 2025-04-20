package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.IconUI;
import org.lwjgl.util.Dimension;

import static mcp.mobius.waila.api.SpecialChars.WailaIcon;
import static mcp.mobius.waila.api.SpecialChars.WailaStyle;

/**
 * Custom renderer for icons from the icons.png file.
 * Syntax: {waila.icon, icon}
 */
public class TTRenderIcon implements ITooltipRenderer {

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(8, 8);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        DisplayUtil.renderIcon(x, y, 8, 8, IconUI.bySymbol(WailaStyle + WailaIcon + params[0]));
    }

}
