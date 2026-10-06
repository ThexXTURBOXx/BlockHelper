package mcp.mobius.waila.addons.forge;

import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.api.LiquidData;
import net.minecraftforge.liquids.ITankContainer;

public class TankRemover implements ITankProvider {

    public static final ITankProvider INSTANCE = new TankRemover();

    private TankRemover() {
    }

    @Override
    public List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config) {
        return null;
    }

}
