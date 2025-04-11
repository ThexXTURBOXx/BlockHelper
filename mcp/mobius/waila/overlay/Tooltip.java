package mcp.mobius.waila.overlay;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import org.lwjgl.opengl.GL11;

import static mcp.mobius.waila.api.SpecialChars.ALIGNCENTER;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.patternIcon;
import static mcp.mobius.waila.api.SpecialChars.patternLineSplit;
import static mcp.mobius.waila.api.SpecialChars.patternRender;
import static mcp.mobius.waila.api.SpecialChars.patternTab;

public class Tooltip {
    public static int TabSpacing = 8;
    public static int IconSize = 8;

    List<List<String>> lines = new ArrayList<List<String>>();
    List<List<Integer>> sizes = new ArrayList<List<Integer>>();
    List<Integer> columnsWidth = new ArrayList<Integer>();
    List<Integer> columnsPos = new ArrayList<Integer>();

    List<Renderable> elements = new ArrayList<Renderable>();
    List<Renderable> elements2nd = new ArrayList<Renderable>();

    int w, h, x, y, ty;
    int offsetX;
    int maxStringW;
    Point pos;
    boolean hasIcon = false;
    ItemStack stack;

    ICommonAccessor accessor = DataAccessorCommon.INSTANCE;

    /// //////////////////////////////////Renderable///////////////////////////////////////
    private static class Renderable {
        final ITooltipRenderer renderer;
        final Point pos;
        final String[] params;

        public Renderable(ITooltipRenderer renderer, Point pos, String[] params) {
            this.renderer = renderer;
            this.pos = pos;
            this.params = params;
        }

        public Renderable(ITooltipRenderer renderer, Point pos) {
            this(renderer, pos, new String[]{});
        }

        public Point getPos() {
            return this.pos;
        }

        public Dimension getSize(ICommonAccessor accessor) {
            Dimension dim = new Dimension(0, 0);
            try {
                dim = this.renderer.getSize(this.params, accessor);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, this.renderer.getClass().getName() + ".getSize()", null);
            }
            return dim;
        }

        public void draw(ICommonAccessor accessor, int x, int y) {
            GL11.glPushMatrix();
            try {
                this.renderer.draw(this.params, accessor, this.pos.x + x, this.pos.y + y);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, this.renderer.getClass().getName() + ".draw()", null);
            }
            GL11.glPopMatrix();
        }

        @Override
        public String toString() {
            return String.format("Renderable@[%d,%d] | %s", pos.x, pos.y, renderer);
        }
    }

    /// /////////////////////////////////////////////////////////////////////////


    public Tooltip(List<String> textData, ItemStack stack) {
        this(textData, stack, stack != null);
    }

    public Tooltip(List<String> textData, ItemStack stack, boolean hasIcon) {
        this.stack = stack;

        columnsWidth.add(0);        // Small init of the arrays to have at least one element
        columnsPos.add(0);

        for (String s : textData) {

            List<String> line = new ArrayList<String>(Arrays.asList(patternTab.split(s)));
            List<Integer> size = new ArrayList<Integer>();
            for (String ss : line)
                size.add(DisplayUtil.getDisplayWidth(ss));

            // This line.size() > 1 is to prevent columns to align on lines without column (ie : the name & modid)
            if (line.size() > 1) {
                while (columnsWidth.size() < line.size()) {
                    columnsWidth.add(0);
                    columnsPos.add(0);
                }

                for (int i = 0; i < line.size(); i++)
                    columnsWidth.set(i, Math.max(columnsWidth.get(i), size.get(i)));
            }

            maxStringW = Math.max(maxStringW, DisplayUtil.getDisplayWidth(s) + TabSpacing * (line.size() - 1));

            lines.add(line);
            sizes.add(size);
        }

        // We correct if we only have one column
        if (columnsWidth.size() == 1)
            columnsWidth.set(0, maxStringW);

        // We compute the position of the columns to be able to align the renderable later on
        for (int i = 1; i < columnsWidth.size(); i++)
            columnsPos.set(i, columnsWidth.get(i - 1) + columnsPos.get(i - 1) + TabSpacing);

        this.computeRenderables();
        this.computePositionAndSize(hasIcon);
    }

    private void computeRenderables() {
        int offsetY = 0;
        for (List<String> line : lines) {                // We check all the lines, one by one
            int maxHeight = 0;                                // Maximum height of this line
            for (int c = 0; c < line.size(); c++) {    // We check all the columns for this line
                offsetX = columnsPos.get(c);            // We move the "cursor" to the current column
                String currentLine = line.get(c);
                Matcher lineMatcher = patternLineSplit.matcher(currentLine);

                while (lineMatcher.find()) {
                    String cs = lineMatcher.group();
                    Renderable renderable = null;
                    Matcher renderMatcher = patternRender.matcher(cs);    //We keep a matcher here to be able to
                    // check if we have a Renderer. Might be better to do a startWith + full matcher init after the
                    // check
                    Matcher iconMatcher = patternIcon.matcher(cs);

                    if (renderMatcher.find()) {
                        String renderName = renderMatcher.group(1);

                        ITooltipRenderer renderer = WailaRegistrar.instance().getTooltipRenderer(renderName);
                        if (renderer != null) {
                            renderable = new Renderable(renderer, new Point(offsetX, offsetY),
                                    renderMatcher.group(2).split(","));
                            this.elements2nd.add(renderable);
                        }
                    } else if (iconMatcher.find()) {
                        renderable = new Renderable(new TTRenderIcon(iconMatcher.group(1)), new Point(offsetX,
                                offsetY));
                        this.elements2nd.add(renderable);
                    } else {
                        if (cs.startsWith(ALIGNRIGHT))
                            offsetX += columnsWidth.get(c) - DisplayUtil.getDisplayWidth(currentLine.substring(lineMatcher.start()));

                        if (cs.startsWith(ALIGNCENTER))
                            offsetX += (columnsWidth.get(c) - DisplayUtil.getDisplayWidth(currentLine.substring(lineMatcher.start()))) / 2;

                        renderable = new Renderable(new TTRenderString(DisplayUtil.stripWailaSymbols(cs)),
                                new Point(offsetX, offsetY));
                        this.elements.add(renderable);
                    }

                    if (renderable != null) {
                        offsetX += renderable.getSize(accessor).width;
                        maxHeight = Math.max(maxHeight, renderable.getSize(accessor).height + 2);
                    }
                }
            }
            offsetY += maxHeight;
        }
    }

    private int getRenderableTotalHeight() {
        int result = 0;
        for (Renderable r : this.elements)
            result = Math.max(r.getPos().y + r.getSize(accessor).height + 2, result);
        return result;
    }

    private void computePositionAndSize(boolean hasIcon) {
        this.pos = new Point(PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_POSX, 0),
                PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 0));
        this.hasIcon = hasIcon;

        int paddingW = hasIcon ? 29 : 13;
        int paddingH = hasIcon ? 24 : 0;
        offsetX = hasIcon ? 24 : 6;

        w = maxStringW + paddingW;

        h = Math.max(paddingH, this.getRenderableTotalHeight() + 8);

        Dimension size = DisplayUtil.displaySize();
        x = ((int) (size.width / OverlayConfig.scale) - w - 1) * pos.x / 10000;
        y = ((int) (size.height / OverlayConfig.scale) - h - 1) * pos.y / 10000;

        ty = (h - this.getRenderableTotalHeight()) / 2 + 1;
    }

    public void draw() {
        for (Renderable r : this.elements)
            r.draw(accessor, x + offsetX, y + ty);
    }

    public void draw2nd() {
        for (Renderable r : this.elements2nd)
            r.draw(accessor, x + offsetX, y + ty);
    }
}
