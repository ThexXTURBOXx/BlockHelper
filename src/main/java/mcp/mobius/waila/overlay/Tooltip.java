package mcp.mobius.waila.overlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderIcon;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderString;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import static mcp.mobius.waila.api.SpecialChars.ALIGNCENTER;
import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.patternIcon;
import static mcp.mobius.waila.api.SpecialChars.patternLineSplit;
import static mcp.mobius.waila.api.SpecialChars.patternRender;
import static mcp.mobius.waila.api.SpecialChars.patternTab;

public class Tooltip {

    public static final int TabSpacing = 8;
    public static final int IconSize = 8;

    final List<List<String>> lines = new ArrayList<List<String>>();
    final List<List<Integer>> sizes = new ArrayList<List<Integer>>();
    final List<Integer> columnsWidth = new ArrayList<Integer>();
    final List<Integer> columnsPos = new ArrayList<Integer>();

    final List<Renderable> elements = new ArrayList<Renderable>();
    final List<Renderable> elements2nd = new ArrayList<Renderable>();

    Rectangle pos;
    int offsetX, offsetY;
    int maxStringW;
    boolean hasIcon = false;
    ItemStack stack;

    static final ICommonAccessor accessor = DataAccessorCommon.INSTANCE;

    /// //////////////////////////////////Renderable///////////////////////////////////////
    private static class Renderable {

        final ITooltipRenderer renderer;
        final Point pos;
        final String[] params;

        public Renderable(ITooltipRenderer renderer, Point pos, String... params) {
            this.renderer = renderer;
            this.pos = pos;
            this.params = params;
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
                this.renderer.draw(this.params, accessor, this.pos.getX() + x, this.pos.getY() + y);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, this.renderer.getClass().getName() + ".draw()", null);
            }
            GL11.glPopMatrix();
        }

        @Override
        public String toString() {
            return "Renderable@[" + pos.getX() + "," + pos.getY() + "] | " + renderer;
        }

    }

    /// /////////////////////////////////////////////////////////////////////////


    public Tooltip(ITaggedList<String, String> textData, ItemStack stack) {
        this(textData, stack, stack != null);
    }

    public Tooltip(ITaggedList<String, String> textData, ItemStack stack, boolean hasIcon) {
        WailaTooltipEvent event = new WailaTooltipEvent(textData, DataAccessorCommon.INSTANCE);
        MinecraftForge.EVENT_BUS.post(event);

        this.stack = stack;

        columnsWidth.add(0);        // Small init of the arrays to have at least one element
        columnsPos.add(0);

        for (String s : textData) {

            List<String> line = new ArrayList<String>(Arrays.asList(patternTab.split(s)));
            List<Integer> size = new ArrayList<Integer>();
            for (String ss : line)
                size.add(DisplayUtil.getDisplayWidth(ss));

            // This line.size() > 1 is to prevent columns to align on lines without column (ie: the name & modid)
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

        int tmp = 0;
        for (int i = 0; i < columnsWidth.size(); i++) {
            tmp += columnsWidth.get(i);

            // We compute the position of the columns to be able to align the renderable later on
            if (i != 0)
                columnsPos.set(i, columnsWidth.get(i - 1) + columnsPos.get(i - 1) + TabSpacing);
        }

        // We correct for edge cases where the longest string in a column is in a line shorter than the longest line
        tmp += TabSpacing * (columnsWidth.size() - 1);
        maxStringW = Math.max(maxStringW, tmp);

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
                        renderable = new Renderable(new TTRenderIcon(), new Point(offsetX, offsetY),
                                iconMatcher.group(1));
                        this.elements2nd.add(renderable);
                    } else {
                        if (cs.startsWith(ALIGNRIGHT))
                            offsetX += columnsWidth.get(c) - DisplayUtil.getDisplayWidth(currentLine.substring(lineMatcher.start()));

                        if (cs.startsWith(ALIGNCENTER))
                            offsetX += (columnsWidth.get(c) - DisplayUtil.getDisplayWidth(currentLine.substring(lineMatcher.start()))) / 2;

                        renderable = new Renderable(new TTRenderString(), new Point(offsetX, offsetY),
                                DisplayUtil.stripWailaSymbols(cs));
                        this.elements.add(renderable);
                    }

                    if (renderable != null) {
                        offsetX += renderable.getSize(accessor).getWidth();
                        maxHeight = Math.max(maxHeight, renderable.getSize(accessor).getHeight() + 2);
                    }
                }
            }
            offsetY += maxHeight;
        }
    }

    private int getRenderableTotalHeight() {
        int result = 0;
        for (Renderable r : this.elements)
            result = Math.max(r.getPos().getY() + r.getSize(accessor).getHeight() + 2, result);
        return result;
    }

    private void computePositionAndSize(boolean hasIcon) {
        int x = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSX, 0);
        int y = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 0);

        this.hasIcon = hasIcon;
        int paddingW = hasIcon ? 29 : 13;
        int paddingH = hasIcon ? 24 : 0;

        int w = maxStringW + paddingW;
        int h = Math.max(paddingH, this.getRenderableTotalHeight() + 8);

        Dimension size = DisplayUtil.displaySize();
        x = ((int) (size.getWidth() / OverlayConfig.scale) - w - 1) * x / 10000;
        y = ((int) (size.getHeight() / OverlayConfig.scale) - h - 1) * y / 10000;

        this.pos = new Rectangle(x, y, w, h);

        this.offsetX = hasIcon ? 24 : 6;
        this.offsetY = (h - this.getRenderableTotalHeight()) / 2 + 1;
    }

    public boolean hasItem() {
        return this.hasIcon && this.stack != null && this.stack.getItem() != null;
    }

    public void drawAll() {
        draw();
        draw2nd();
    }

    public void draw() {
        for (Renderable r : this.elements)
            r.draw(accessor, this.pos.getX() + offsetX, this.pos.getY() + offsetY);
    }

    public void draw2nd() {
        for (Renderable r : this.elements2nd)
            r.draw(accessor, this.pos.getX() + offsetX, this.pos.getY() + offsetY);
    }

}
