package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IVariableWidthTooltipRenderer;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.NumberFormatter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for fancy liquid bars.
 * Syntax: {waila.liquid, liquidName, localizedName, amount, capacity}
 */
public class TTRenderLiquidBar implements IVariableWidthTooltipRenderer {

    public static final String EMPTY_LIQUID = "EMPTYLIQUID";
    public static final String GRADIENT_TEXTURE = "/assets/waila/textures/gradient.png";

    private static final int height = 12;

    private int maxStringW;

    public void bindColor(String liquidName) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        boolean isEmpty = (params[0].equals(EMPTY_LIQUID) && params[1].equals(EMPTY_LIQUID));
        int displayWidth = DisplayUtil.getDisplayWidth(
                buildDisplayText(
                        isEmpty ? 0 : Double.parseDouble(params[2]),
                        Double.parseDouble(params[3]),
                        params[1],
                        isEmpty));

        return new Dimension(displayWidth + 4, height);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        String liquidName = params[0];
        String localizedName = params[1];
        double amount = Double.parseDouble(params[2]);
        double capacity = Double.parseDouble(params[3]);
        Tessellator tessellator = Tessellator.instance;
        boolean isEmpty = liquidName.equals(EMPTY_LIQUID) && localizedName.equals(EMPTY_LIQUID);

        Minecraft mc = Minecraft.getMinecraft();
        if (!isEmpty) {
            LiquidStack stack = LiquidDictionary.getLiquid(liquidName, (int) amount);
            ItemStack is = stack.asItemStack();
            if (is == null || is.getItem() == null) is = new ItemStack(Block.waterStill);

            int index = is.getIconIndex();
            float minU = (index % 16 * 16 + 0) / 256.0F;
            float maxU = (index % 16 * 16 + 16) / 256.0F;
            float minV = (index / 16 * 16 + 0) / 256.0F;
            float maxV = (index / 16 * 16 + 16) / 256.0F;

            mc.renderEngine.bindTexture(mc.renderEngine.getTexture(is.getItem().getTextureFile()));

            bindColor(liquidName);

            tessellator.startDrawingQuads();
            // Intentionally draw 2 pixels taller than needed than cover with the border to
            // make the texture more visible
            int i = (int) ((double) (maxStringW - 2) * amount / capacity);
            int j = 0;
            for (; i > height; i = i - height) {
                DisplayUtil.drawRectIntern(tessellator, x + 1 + (j * height), y, 0, height, height,
                        minU, minV, maxU, maxV);
                j++;
            }
            if (i > 0) DisplayUtil.drawRectIntern(
                    tessellator,
                    x + 1 + (j * height),
                    y,
                    0,
                    i,
                    height,
                    minU,
                    minV,
                    minU + ((maxU - minU) * ((double) i / height)),
                    maxV);
            tessellator.draw();
        }

        if (!isEmpty) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1F, 1F, 1F, 0.70F);
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture(GRADIENT_TEXTURE));
            tessellator.startDrawingQuads();
            DisplayUtil.drawRectIntern(tessellator, x + 1, y, 0, maxStringW - 2, height - 1, 0, 0, 1, 1);
            tessellator.draw();
        } else {
            Gui.drawRect(x + 1, y, x + maxStringW - 1, y + height - 1, 0x1A575656);
        }

        DisplayUtil.drawThickBeveledBox(x, y, x + maxStringW, y + height, 1, 0xFF505050, 0xFF505050, -1);

        DisplayUtil.drawString(
                buildDisplayText(amount, capacity, localizedName, isEmpty),
                x + 2,
                y + 2,
                isEmpty ? 0xFFDDDDDD : 0xFFFFFFFF,
                true);
    }

    public String buildDisplayText(double amount, double capacity, String liquidName, boolean isEmpty) {
        return isEmpty
                ? String.format(
                "%s / %s %s",
                I18n.translate("hud.msg.empty"),
                NumberFormatter.format((int) capacity),
                PluginConfig.instance().liquidUnit)
                : String.format(
                "%s / %s %s %s",
                NumberFormatter.format((int) amount),
                NumberFormatter.format((int) capacity),
                PluginConfig.instance().liquidUnit,
                liquidName);
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
