package mcp.mobius.waila.addons.bc3;

import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;

public class TankRemover implements ITankProvider {

    public static final ITankProvider INSTANCE = new TankRemover();

    private TankRemover() {
    }

    @Override
    public List<Object> getTanks(Object container, IServerCommonAccessor accessor, IPluginConfig config) {
        return null;
    }

}
