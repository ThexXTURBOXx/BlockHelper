package mcp.mobius.waila.addons.nilfix;

import de.femtopedia.nilb173fix.api.TooltipAPI;
import mcp.mobius.waila.api.impl.WailaRegistrar;

public final class NilFixHandler {

    private NilFixHandler() {
        throw new UnsupportedOperationException();
    }

    public static void register() {
        TooltipAPI.instance().registerHandler(TooltipHandlerWaila.INSTANCE);
        WailaRegistrar.instance().addConfig("NilFix", "nilfix.modtooltips");
    }

}
