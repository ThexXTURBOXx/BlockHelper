package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IVariableWidthTooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.NumberFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for fancy energy bars.
 * Syntax: {waila.energy, amount, capacity, unit}
 */
public class TTRenderEnergyBar implements IVariableWidthTooltipRenderer {

    public static final String BAR_TEXTURE = "/assets/waila/textures/energy_bar.png";
    public static final String GRADIENT_TEXTURE = "/assets/waila/textures/gradient.png";

    private static final int height = 12;
    private static final int width = 2;

    private int maxStringW;

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(
                DisplayUtil.getDisplayWidth(buildDisplayText(
                        Integer.parseInt(params[0]), Integer.parseInt(params[1]), params[2]))
                + 4,
                height);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int amount = Integer.parseInt(params[0]);
        int capacity = Integer.parseInt(params[1]);
        String unit = params[2];
        Tessellator tessellator = Tessellator.instance;

        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(mc.renderEngine.getTexture(BAR_TEXTURE));

        GL11.glColor4f(1F, 1F, 1F, 1F);
        tessellator.startDrawingQuads();

        // Draw dark (uncharged) background for whole bar first
        for (int i = 0; i < (maxStringW - 2); i += width) {
            DisplayUtil.drawRectIntern(tessellator, x + 1 + i, y, 0, width, height,
                    0.0, 0.0, 0.5, 1.0);
        }

        double i = (double) (maxStringW - 2) * amount / capacity;
        int drawnRects = 0;
        for (; i > width; i -= width) {
            DisplayUtil.drawRectIntern(tessellator, x + 1 + (drawnRects * width), y, 0, width, height,
                    0.5, 0.0, 1.0, 1.0);
            drawnRects++;
        }
        // Do less than full increments just as much as they take up on the scaled texture
        DisplayUtil.drawRectIntern(tessellator, x + 1 + (drawnRects * width), y, 0, i, height,
                0.5, 0.0, 0.5 + 0.25 * i, 1.0);
        tessellator.draw();

        // Border
        DisplayUtil.drawThickBeveledBox(x, y, x + maxStringW, y + height, 1, 0xFF505050, 0xFF505050, -1);

        // Gradient
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 0.5F);
        mc.renderEngine.bindTexture(mc.renderEngine.getTexture(GRADIENT_TEXTURE));
        tessellator.startDrawingQuads();
        DisplayUtil.drawRectIntern(tessellator, x + 1, y + 1, 0, maxStringW - 2, height - 2, 0, 0, 1, 1);
        tessellator.draw();

        DisplayUtil.drawString(buildDisplayText(amount, capacity, unit), x + 2, y + 2, 0xFFFFFFFF, true);
    }

    public String buildDisplayText(int amount, int capacity, String unit) {
        return String.format("%s / %s %s", NumberFormatter.format(amount), NumberFormatter.format(capacity), unit);
    }

    @Override
    public void setMaxLineWidth(int width) {
        maxStringW = width + 2;
    }

    @Override
    public int getMaxLineWidth() {
        return maxStringW;
    }

}
