package mcp.mobius.waila.overlay.tooltiprenderers;

import java.awt.Dimension;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.client.Minecraft;

public class TTRenderProgressBar implements ITooltipRenderer {

    Minecraft mc = Minecraft.getMinecraft();
    String texture = "/assets/waila/textures/sprites.png";

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(32, 16);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int currentValue = Integer.parseInt(params[0]);
        int maxValue = Integer.parseInt(params[1]);

        int progress = (currentValue * 28) / maxValue;

        this.mc.renderEngine.bindTexture(texture);

        DisplayUtil.drawTexturedModalRect(x + 4, y, 4, 16, 28, 16, 28, 16);
        DisplayUtil.drawTexturedModalRect(x + 4, y, 4, 0, progress + 1, 16, progress + 1, 16);

    }

}
