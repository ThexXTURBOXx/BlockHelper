package mcp.mobius.waila.overlay;

import mcp.mobius.waila.api.event.WailaEventRegistrar;
import mcp.mobius.waila.api.event.WailaRenderEvent;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.GLState;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.GuiChat;
import net.minecraft.src.ModLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public final class OverlayRenderer {

    private OverlayRenderer() {
        throw new UnsupportedOperationException();
    }

    public static void renderOverlay(Tooltip tooltip) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) || // No open screen, except chat
            mc.theWorld == null || // World is loaded
            !Minecraft.isGuiEnabled() || // Not in cinema mode
            (mc.gameSettings.showDebugInfo // Together with next line: handle F3 screen
             && PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_HIDE_IN_DEBUG, true)) ||
            !PluginConfig.instance().showTooltip() || // Tooltip is enabled in config
            RayTracing.instance().getTarget() == null) // Raytrace found a target
            return;

        if (RayTracing.instance().getTarget().typeOfHit == EnumMovingObjectType.TILE && RayTracing.instance().getTargetStack() != null) {
            doRenderOverlay(tooltip);
        }

        if (RayTracing.instance().getTarget().typeOfHit == EnumMovingObjectType.ENTITY && PluginConfig.instance().get("general.showents")) {
            doRenderOverlay(tooltip);
        }
    }

    private static void doRenderOverlay(Tooltip tooltip) {
        GLState state = new GLState();

        draw:
        try {
            GL11.glScalef(OverlayConfig.scale, OverlayConfig.scale, 1.0f);

            WailaRenderEvent.Pre preEvent = new WailaRenderEvent.Pre(tooltip.pos, DataAccessorCommon.INSTANCE,
                    OverlayConfig.bgcolor, OverlayConfig.gradient1, OverlayConfig.gradient2);
            if (WailaEventRegistrar.postPreRender(preEvent)) break draw;
            Rectangle position = preEvent.getPosition();

            drawTooltipBox(position, preEvent.getBackground(), preEvent.getGradientStart(), preEvent.getGradientEnd());

            tooltip.drawAll();

            if (tooltip.hasItem()) {
                int align = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_ICON_ALIGN, 1);

                int y = position.getY() + (align == 0 ? 5
                        : align == 2 ? position.getHeight() - 19
                        : position.getHeight() / 2 - 8);

                DisplayUtil.renderStack(position.getX() + 5, y, tooltip.stack);
            }

            WailaRenderEvent.Post postEvent = new WailaRenderEvent.Post(position);
            WailaEventRegistrar.postPostRender(postEvent);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, tooltip.getClass(), null);
        }

        state.reset();
    }

    private static void drawTooltipBox(Rectangle position, int bg, int grad1, int grad2) {
        drawTooltipBox(position.getX(), position.getY(), position.getWidth(), position.getHeight(), bg, grad1, grad2);
    }

    private static void drawTooltipBox(int x, int y, int w, int h, int bg, int grad1, int grad2) {
        DisplayUtil.drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        DisplayUtil.drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        DisplayUtil.drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);//center
        DisplayUtil.drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        DisplayUtil.drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        DisplayUtil.drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        DisplayUtil.drawGradientRect(x + w - 1, y + 2, 1, h - 3, grad1, grad2);
        DisplayUtil.drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        DisplayUtil.drawGradientRect(x + 1, y + h - 1, w - 1, 1, grad2, grad2);
    }

}
