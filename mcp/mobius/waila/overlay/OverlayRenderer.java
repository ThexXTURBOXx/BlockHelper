package mcp.mobius.waila.overlay;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.GLState;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumMovingObjectType;
import org.lwjgl.opengl.GL11;

public final class OverlayRenderer {

    private OverlayRenderer() {
        throw new UnsupportedOperationException();
    }

    public static void renderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen == null &&
              mc.theWorld != null &&
              Minecraft.isGuiEnabled() &&
              !mc.gameSettings.keyBindPlayerList.pressed &&
              PluginConfig.instance().showTooltip() &&
              RayTracing.instance().getTarget() != null))
            return;

        if (RayTracing.instance().getTarget().typeOfHit == EnumMovingObjectType.TILE && RayTracing.instance().getTargetStack() != null) {
            renderOverlay(mod_BlockHelper.TICK_HANDLER.tooltip);
        }

        if (RayTracing.instance().getTarget().typeOfHit == EnumMovingObjectType.ENTITY && PluginConfig.instance().get("general.showents")) {
            renderOverlay(mod_BlockHelper.TICK_HANDLER.tooltip);
        }
    }

    public static void renderOverlay(Tooltip tooltip) {
        GL11.glPushMatrix();
        GLState state = new GLState();

        try {
            GL11.glScalef(OverlayConfig.scale, OverlayConfig.scale, 1.0f);

            drawTooltipBox(tooltip.x, tooltip.y, tooltip.w, tooltip.h, OverlayConfig.bgcolor,
                    OverlayConfig.gradient1, OverlayConfig.gradient2);

            tooltip.draw();

            tooltip.draw2nd();

            if (tooltip.hasIcon && tooltip.stack != null && tooltip.stack.getItem() != null)
                DisplayUtil.renderStack(tooltip.x + 5, tooltip.y + tooltip.h / 2 - 8, tooltip.stack);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "renderOverlay", null);
        }

        state.reset();
        GL11.glPopMatrix();
    }

    public static void drawTooltipBox(int x, int y, int w, int h, int bg, int grad1, int grad2) {
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
