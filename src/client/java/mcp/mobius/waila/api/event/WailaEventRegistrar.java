package mcp.mobius.waila.api.event;

import java.util.ArrayList;
import java.util.List;

public final class WailaEventRegistrar {

    private static final List<IWailaEventListener> LISTENERS = new ArrayList<IWailaEventListener>();

    private WailaEventRegistrar() {
        throw new UnsupportedOperationException();
    }

    public static void register(IWailaEventListener listener) {
        LISTENERS.add(listener);
    }

    public static boolean postPreRender(WailaRenderEvent.Pre event) {
        for (IWailaEventListener listener : LISTENERS) {
            listener.onWailaRenderPre(event);
        }
        return event.isCancelled();
    }

    public static void postPostRender(WailaRenderEvent.Post event) {
        for (IWailaEventListener listener : LISTENERS) {
            listener.onWailaRenderPost(event);
        }
    }

    public static void postTooltip(WailaTooltipEvent event) {
        for (IWailaEventListener listener : LISTENERS) {
            listener.onWailaTooltip(event);
        }
    }

}
