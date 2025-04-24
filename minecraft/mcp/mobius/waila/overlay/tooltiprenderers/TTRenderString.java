package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.OverlayConfig;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for standard strings.
 * Syntax: {waila.string, string}
 */
public class TTRenderString implements ITooltipRenderer {

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        String data = getData(params);
        return new Dimension(DisplayUtil.getDisplayWidth(data), data.isEmpty() ? 0 : 8);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        DisplayUtil.drawString(getData(params), x, y, OverlayConfig.fontcolor, true);
    }

    private String getData(String[] params) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i != 0) ret.append(",");
            ret.append(params[i]);
        }
        return ret.toString();
    }

}
