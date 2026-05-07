package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for progress bars.
 * Syntax: {waila.progress, currentvalue, maxvalue}
 */
public class TTRenderProgressBar implements ITooltipRenderer {

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(32, 16);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int currentValue = Integer.parseInt(params[0]);
        int maxValue = Integer.parseInt(params[1]);

        int progress = (currentValue * 28) / maxValue;

        mod_BlockHelper.minecraft.renderEngine.bindTexture(
                mod_BlockHelper.minecraft.renderEngine.getTexture("/assets/waila/textures/sprites.png"));

        DisplayUtil.drawTexturedModalRect(x + 4, y, 4, 16, 28, 16, 28, 16);
        DisplayUtil.drawTexturedModalRect(x + 4, y, 4, 0, progress + 1, 16, progress + 1, 16);
    }

    public static String create(int cur, int max) {
        return SpecialChars.getRenderString("waila.progress",
                cur, max);
    }

}
