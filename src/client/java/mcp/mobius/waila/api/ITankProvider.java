package mcp.mobius.waila.api;

import java.util.List;

public interface ITankProvider {

    /**
     * Simple {@link ITankProvider} that removes all tank information.
     */
    ITankProvider REMOVER = new TankRemover();

    /**
     * Server-side callback used to provide or override current tank behavior.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerTankProvider} server side.
     *
     * @param container Current tank container scanned (is assumed to be of a tank container type).
     * @param accessor  Contains most of the relevant information about the current environment.
     *                  Can be casted to its respective descendants, if needed.
     * @param config    Current configuration of Waila.
     * @return The current {@link LiquidData} to show in the tooltip.
     */
    List<LiquidData> getTanks(Object container, IServerCommonAccessor accessor, IPluginConfig config);


    class TankRemover implements ITankProvider {

        private TankRemover() {
        }

        @Override
        public List<LiquidData> getTanks(Object container, IServerCommonAccessor accessor, IPluginConfig config) {
            return null;
        }

    }

}
