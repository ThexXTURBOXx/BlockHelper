package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IVariableWidthTooltipRenderer;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.overlay.DisplayUtil;
import org.lwjgl.util.Dimension;

public class TTRenderBar implements IVariableWidthTooltipRenderer {

    private static final int HEIGHT = 12;

    private int maxStringW;

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        String text = params[0];

        int displayWidth = DisplayUtil.getDisplayWidth(text);

        return new Dimension(displayWidth + 4, HEIGHT);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        String text = params[0];
        int topColor = 0xFFFFEE55;
        int bottomColor = 0xFFFFA500;
        int fillWidth = maxStringW - 2;

        if (params.length > 2) {
            try {
                topColor = Integer.parseInt(params[1]);
                bottomColor = Integer.parseInt(params[2]);

            } catch (NumberFormatException ignored) {
            }
        }

        if (params.length > 3) {
            try {
                double ratio = Double.parseDouble(params[3]);
                ratio = Math.max(0.0, Math.min(1.0, ratio));
                fillWidth = (int) ((maxStringW - 2) * ratio);
            } catch (NumberFormatException ignored) {
            }
        }

        DisplayUtil.drawGradientRect(x + 1, y, maxStringW - 1, HEIGHT - 1, 0xFF2A2A2A, 0xFF111111);

        if (fillWidth > 0) {
            DisplayUtil.drawGradientRect(x + 1, y, fillWidth, HEIGHT - 1, topColor, bottomColor);
        }

        DisplayUtil.drawThickBeveledBox(x, y, x + maxStringW, y + HEIGHT, 1, 0xFF505050, 0xFF505050, -1);
        DisplayUtil.drawString(text, x + 2, y + 2, 0xFFFFFFFF, true);
    }

    @Override
    public void setMaxLineWidth(int width) {
        maxStringW = width;
    }

    @Override
    public int getMaxLineWidth() {
        return maxStringW;
    }

    public static String create(String text, int topColor, int bottomColor, double progress) {
        return SpecialChars.getRenderString(
                "waila.bar",
                text,
                String.valueOf(topColor),
                String.valueOf(bottomColor),
                String.valueOf(progress));
    }

    public static String create(String text, int color, double progress) {
        return create(text, color, color, progress);
    }

}
