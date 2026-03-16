package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IVariableWidthTooltipRenderer;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.NumberFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for fancy liquid bars.
 * Syntax: {waila.liquid, liquidId, liquidMeta, localizedName, amount, capacity}
 */
public class TTRenderLiquidBar implements IVariableWidthTooltipRenderer {

    public static final int EMPTY_LIQUID = -1;
    public static final String GRADIENT_TEXTURE = "/assets/waila/textures/gradient.png";

    private static final int height = 12;

    private int maxStringW;

    public void bindColor(ItemStack itemStack) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        boolean isEmpty = Integer.parseInt(params[0]) == EMPTY_LIQUID;
        int displayWidth = DisplayUtil.getDisplayWidth(
                buildDisplayText(
                        isEmpty ? 0 : Double.parseDouble(params[3]),
                        Double.parseDouble(params[4]),
                        params[2],
                        isEmpty));

        return new Dimension(displayWidth + 4, height);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int liquidId = Integer.parseInt(params[0]);
        int liquidMeta = Integer.parseInt(params[1]);
        String localizedName = params[2];
        double amount = Double.parseDouble(params[3]);
        double capacity = Double.parseDouble(params[4]);
        Tessellator tessellator = Tessellator.instance;
        boolean isEmpty = liquidId == EMPTY_LIQUID;

        Minecraft mc = ModLoader.getMinecraftInstance();
        if (!isEmpty) {
            ItemStack is = new ItemStack(liquidId, (int) amount, liquidMeta);
            if (is.getItem() == null) is = new ItemStack(Block.waterStill);

            int index = is.getIconIndex();
            float minU = (index % 16 * 16 + 0) / 256.0F;
            float maxU = (index % 16 * 16 + 16) / 256.0F;
            float minV = (index / 16 * 16 + 0) / 256.0F;
            float maxV = (index / 16 * 16 + 16) / 256.0F;

            String tex = is.itemID < 256 ? "/terrain.png" : "/gui/items.png";
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture(tex));

            bindColor(is);

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
            UIHelper.drawRect(x + 1, y, x + maxStringW - 1, y + height - 1, 0, 0x1A575656);
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

    public static String create(int liquidId, int liquidMeta, String localizedName,
                                int amount, int capacity) {
        return SpecialChars.getRenderString("waila.liquid",
                liquidId, liquidMeta, localizedName, amount, capacity);
    }

    public static String createEmpty(int capacity) {
        return create(TTRenderLiquidBar.EMPTY_LIQUID, 0, "", 0, capacity);
    }

}
