package mcp.mobius.waila.api;

import java.util.List;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public interface ITankProvider {

    /**
     * Server-side callback used to provide or override current tank behavior.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerTankProvider} server side.
     *
     * @param container Current tank container scanned.
     * @param accessor  Contains most of the relevant information about the current environment.
     *                  Can be casted to its respective descendants, if needed.
     * @param config    Current configuration of Waila.
     * @return The current tanks to show in the tooltip.
     */
    List<ILiquidTank> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config);

}
