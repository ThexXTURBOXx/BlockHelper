package mcp.mobius.waila.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.Dimension;

import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.MCStyle;
import static mcp.mobius.waila.api.SpecialChars.WailaRendererComma;
import static mcp.mobius.waila.api.SpecialChars.patternIcon;
import static mcp.mobius.waila.api.SpecialChars.patternMinecraft;
import static mcp.mobius.waila.api.SpecialChars.patternRender;
import static mcp.mobius.waila.api.SpecialChars.patternWaila;

public final class DisplayUtil {

    private static final FontRenderer fontRenderer = ModLoader.getMinecraftInstance().fontRenderer;
    private static final RenderEngine renderEngine = ModLoader.getMinecraftInstance().renderEngine;
    private static final RenderItem renderItem = new RenderItem();

    static {
        renderItem.field_40268_b = 200.0F; // important for enchantment glint
    }

    private DisplayUtil() {
        throw new UnsupportedOperationException();
    }

    public static int getDisplayWidth(String s) {
        if (s == null || s.isEmpty()) return 0;

        int width = 0;

        Matcher renderMatcher = patternRender.matcher(s);
        while (renderMatcher.find()) {
            ITooltipRenderer renderer = WailaRegistrar.instance().getTooltipRenderer(renderMatcher.group(1));
            if (renderer != null)
                width += renderer.getSize(renderMatcher.group(2).split(WailaRendererComma),
                        DataAccessorCommon.INSTANCE).getWidth();
        }

        Matcher iconMatcher = patternIcon.matcher(s);
        while (iconMatcher.find())
            width += 8;

        width += fontRenderer.getStringWidth(stripSymbols(s));
        return width;
    }

    public static Dimension displaySize() {
        Minecraft mc = ModLoader.getMinecraftInstance();
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    public static String stripSymbols(String s) {
        String result = patternRender.matcher(s).replaceAll("");
        result = patternMinecraft.matcher(result).replaceAll("");
        result = patternWaila.matcher(result).replaceAll("");
        return result;
    }

    public static String stripWailaSymbols(String s) {
        String result = patternRender.matcher(s).replaceAll("");
        result = patternWaila.matcher(result).replaceAll("");
        return result;
    }

    public static void renderStack(int x, int y, ItemStack stack) {
        if (stack == null) return;
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1, 240, 240);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        try {
            renderItem.renderItemIntoGUI(fontRenderer, renderEngine, stack, x, y);
            renderItem.renderItemOverlayIntoGUI(fontRenderer, renderEngine, stack, x, y);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, stack.getItem().getClass(), null);
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }

    public static void drawGradientRect(int x, int y, int w, int h, int grad1, int grad2) {
        float zLevel = 0.0f;

        float f = (float) (grad1 >> 24 & 255) / 255.0F;
        float f1 = (float) (grad1 >> 16 & 255) / 255.0F;
        float f2 = (float) (grad1 >> 8 & 255) / 255.0F;
        float f3 = (float) (grad1 & 255) / 255.0F;
        float f4 = (float) (grad2 >> 24 & 255) / 255.0F;
        float f5 = (float) (grad2 >> 16 & 255) / 255.0F;
        float f6 = (float) (grad2 >> 8 & 255) / 255.0F;
        float f7 = (float) (grad2 & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x + w, y, zLevel);
        tessellator.addVertex(x, y, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x, y + h, zLevel);
        tessellator.addVertex(x + w, y + h, zLevel);
        tessellator.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int w, int h, int tw, int th) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float zLevel = 0.0F;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.addVertexWithUV(x, y + h, zLevel, (u) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y + h, zLevel, (u + tw) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y, zLevel, (u + tw) * f, (v) * f1);
        tessellator.addVertexWithUV(x, y, zLevel, (u) * f, (v) * f1);
        tessellator.draw();
    }

    public static void drawRectIntern(Tessellator tessellator, double x, double y, double z,
                                      double width, double height, double minU, double minV, double maxU, double maxV) {
        tessellator.addVertexWithUV(x, y + height, z, minU, maxV);
        tessellator.addVertexWithUV(x + width, y + height, z, maxU, maxV);
        tessellator.addVertexWithUV(x + width, y, z, maxU, minV);
        tessellator.addVertexWithUV(x, y, z, minU, minV);
    }

    public static void drawThickBeveledBox(int x1, int y1, int x2, int y2, int thickness, int topleftcolor,
                                           int botrightcolor, int fillcolor) {
        if (fillcolor != -1) {
            UIHelper.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0, fillcolor);
        }
        UIHelper.drawRect(x1, y1, x2 - 1, y1 + thickness, 0, topleftcolor);
        UIHelper.drawRect(x1, y1, x1 + thickness, y2 - 1, 0, topleftcolor);
        UIHelper.drawRect(x2 - thickness, y1, x2, y2 - 1, 0, botrightcolor);
        UIHelper.drawRect(x1, y2 - thickness, x2, y2, 0, botrightcolor);
    }

    public static void drawString(String text, int x, int y, int colour, boolean shadow) {
        if (shadow) fontRenderer.drawStringWithShadow(text, x, y, colour);
        else fontRenderer.drawString(text, x, y, colour);
    }

    @SuppressWarnings("unchecked")
    public static List<String> itemDisplayNameMultilineUnformatted(ItemStack itemstack) {
        List<String> namelist = null;
        try {
            namelist = (List<String>) itemstack.func_40712_q();
        } catch (Throwable ignored) {
        }

        if (namelist == null)
            namelist = new ArrayList<String>();

        if (namelist.isEmpty())
            namelist.add("Unnamed");

        if (namelist.get(0) == null || namelist.get(0).isEmpty())
            namelist.set(0, "Unnamed");

        return namelist;
    }

    public static String itemDisplayNameShortUnformatted(ItemStack itemstack) {
        return itemDisplayNameMultilineUnformatted(itemstack).get(0);
    }

    public static List<String> itemDisplayNameMultiline(ItemStack itemstack) {
        List<String> namelist = itemDisplayNameMultilineUnformatted(itemstack);

        namelist.set(0, MCStyle + Integer.toHexString(itemstack.func_40707_s().field_40535_e) + namelist.get(0));
        for (int i = 1; i < namelist.size(); i++)
            namelist.set(i, GRAY + namelist.get(i));

        return namelist;
    }

    public static String itemDisplayNameShort(ItemStack itemstack) {
        return itemDisplayNameMultiline(itemstack).get(0);
    }

    public static void renderIcon(int x, int y, int sx, int sy, IconUI icon) {
        if (icon == null) return;

        ModLoader.getMinecraftInstance().renderEngine.bindTexture(
                ModLoader.getMinecraftInstance().renderEngine.getTexture(icon.texture));

        if (icon.bu != -1)
            DisplayUtil.drawTexturedModalRect(x, y, icon.bu, icon.bv, sx, sy, icon.bsu, icon.bsv);
        DisplayUtil.drawTexturedModalRect(x, y, icon.u, icon.v, sx, sy, icon.su, icon.sv);
    }

}
