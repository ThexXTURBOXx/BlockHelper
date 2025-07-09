package mcp.mobius.waila.overlay;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.Constants;
import net.minecraftforge.common.Configuration;

public final class OverlayConfig {

    public static int posX;
    public static int posY;
    public static int alpha;
    public static int bgcolor;
    public static int gradient1;
    public static int gradient2;
    public static int fontcolor;
    public static float scale;

    public static void updateColors() {
        alpha = (int) (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_ALPHA, 0) / 100.0f * 255f) << 24;
        bgcolor = alpha + PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_BGCOLOR, 0);
        gradient1 = alpha + PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_GRADIENT1, 0);
        gradient2 = alpha + PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_GRADIENT2, 0);
        fontcolor = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_FONTCOLOR, 0);
    }

    private OverlayConfig() {
        throw new UnsupportedOperationException();
    }

}
