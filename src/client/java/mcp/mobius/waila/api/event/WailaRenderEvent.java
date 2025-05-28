package mcp.mobius.waila.api.event;

import mcp.mobius.waila.api.ICommonAccessor;
import org.lwjgl.util.Rectangle;

/**
 * The base event for rendering the Waila tooltip. This provides the opportunity to do last minute changes
 * to the tooltip.
 * <p>
 * All sub-events are fired from
 * {@link mcp.mobius.waila.overlay.OverlayRenderer#renderOverlay(mcp.mobius.waila.overlay.Tooltip)}.
 * All sub-events are fired every render tick.
 * <p>
 * {@link #position} The position and size of the tooltip being rendered
 */
public class WailaRenderEvent {

    private final Rectangle position;

    public WailaRenderEvent(Rectangle position) {
        this.position = position;
    }

    public Rectangle getPosition() {
        return position;
    }

    /**
     * This event is fired just before the Waila tooltip is rendered and right after setting up the GL state in
     * {@link mcp.mobius.waila.overlay.OverlayRenderer#renderOverlay(mcp.mobius.waila.overlay.Tooltip)}
     * <p>
     * This event is cancelable.
     * If this event is canceled, the tooltip will not render.
     */
    public static class Pre extends WailaRenderEvent {

        private final ICommonAccessor accessor;
        private int background;
        private int gradientStart;
        private int gradientEnd;
        private boolean cancelled = false;

        public Pre(Rectangle position, ICommonAccessor accessor,
                   int background, int gradientStart, int gradientEnd) {
            super(position);
            this.accessor = accessor;
            this.background = background;
            this.gradientStart = gradientStart;
            this.gradientEnd = gradientEnd;
        }

        public ICommonAccessor getAccessor() {
            return accessor;
        }

        public int getBackground() {
            return background;
        }

        public void setBackground(int background) {
            this.background = background;
        }

        public int getGradientStart() {
            return gradientStart;
        }

        public void setGradientStart(int gradientStart) {
            this.gradientStart = gradientStart;
        }

        public int getGradientEnd() {
            return gradientEnd;
        }

        public void setGradientEnd(int gradientEnd) {
            this.gradientEnd = gradientEnd;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public boolean isCancelled() {
            return cancelled;
        }

    }

    /**
     * This event is fired just after the tooltip is rendered and right before the GL state is reset in
     * {@link mcp.mobius.waila.overlay.OverlayRenderer#renderOverlay(mcp.mobius.waila.overlay.Tooltip)}.
     * This event is only fired if {@link Pre} is not canceled and the draw process did not throw an exception.
     * <p>
     * This event is not cancelable.
     */
    public static class Post extends WailaRenderEvent {

        public Post(Rectangle position) {
            super(position);
        }

    }

}
