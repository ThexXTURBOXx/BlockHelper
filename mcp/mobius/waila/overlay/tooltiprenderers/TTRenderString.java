package mcp.mobius.waila.overlay.tooltiprenderers;

import org.lwjgl.util.Dimension;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.OverlayConfig;

public class TTRenderString implements ITooltipRenderer {

    final String data;
    final Dimension size;

    public TTRenderString(String data) {
        this.data = data;
        this.size = new Dimension(DisplayUtil.getDisplayWidth(data), data.isEmpty() ? 0 : 8);
    }

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return size;
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        DisplayUtil.drawString(data, x, y, OverlayConfig.fontcolor, true);
    }

}
