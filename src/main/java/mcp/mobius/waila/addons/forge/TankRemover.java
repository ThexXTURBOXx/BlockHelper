package mcp.mobius.waila.addons.forge;

import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public class TankRemover implements ITankProvider {

    public static final ITankProvider INSTANCE = new TankRemover();

    private TankRemover() {
    }

    @Override
    public List<ILiquidTank> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config) {
        return null;
    }

}
