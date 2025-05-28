package mcp.mobius.waila.api.event;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITaggedList;

/**
 * This event is fired just before the Waila tooltip sizes are calculated.
 * This is the last chance to make edits to the information being displayed.
 * <p>
 * This event is not cancelable.
 * <p>
 * {@link #currentTip} - The current tooltip to be drawn.
 */
public class WailaTooltipEvent {

    private final ITaggedList<String, String> currentTip;
    private final ICommonAccessor accessor;

    public WailaTooltipEvent(ITaggedList<String, String> currentTip, ICommonAccessor accessor) {
        this.currentTip = currentTip;
        this.accessor = accessor;
    }

    public ITaggedList<String, String> getCurrentTip() {
        return currentTip;
    }

    public ICommonAccessor getAccessor() {
        return accessor;
    }

}
