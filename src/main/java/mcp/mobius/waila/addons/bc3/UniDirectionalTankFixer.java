package mcp.mobius.waila.addons.bc3;

import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.utils.WailaExceptionHandler;

public class UniDirectionalTankFixer implements ITankProvider {

    public static final ITankProvider DEFAULT = new UniDirectionalTankFixer(0);

    private final int extractSingleTank;

    private UniDirectionalTankFixer(int extractSingleTank) {
        this.extractSingleTank = extractSingleTank;
    }

    public static UniDirectionalTankFixer singleTank(int index) {
        return new UniDirectionalTankFixer(index);
    }

    @Override
    public List<Object> getTanks(Object container, IServerCommonAccessor accessor, IPluginConfig config) {
        try {
            Object[] tanks = (Object[]) BC3Plugin.ITankContainer_getTanks.invoke(container);
            if (tanks == null || tanks.length == 0) return Collections.emptyList();
            if (tanks.length <= extractSingleTank) return Collections.emptyList();
            return Collections.singletonList(tanks[extractSingleTank]);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, container.getClass(), null);
        }
        return Collections.emptyList();
    }

}
