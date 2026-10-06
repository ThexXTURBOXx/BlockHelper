package mcp.mobius.waila.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public interface ITankProvider {

    /**
     * Simple {@link ITankProvider} that removes all tank information.
     */
    ITankProvider REMOVER = new TankRemover();

    /**
     * {@link ITankProvider} that removes all tank information except for the one in the "UNKNOWN" face.
     *
     * @see UniDirectionalTankFixer#withDirectionOverride(ForgeDirection) This for specifying the face to display.
     * @see UniDirectionalTankFixer#withDirectionOverrideSingleTank(ForgeDirection, int) This for specifying the face
     *         and tank sub-index to display.
     */
    ITankProvider UNIDIRECTIONAL = new UniDirectionalTankFixer();

    /**
     * Server-side callback used to provide or override current tank behavior.</br>
     * Will be used if the implementing class is registered via {@link IRegistrar#registerTankProvider} server side.
     *
     * @param container Current tank container scanned.
     * @param accessor  Contains most of the relevant information about the current environment.
     *                  Can be casted to its respective descendants, if needed.
     * @param config    Current configuration of Waila.
     * @return The current {@link LiquidData} to show in the tooltip.
     */
    List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config);


    class TankRemover implements ITankProvider {

        private TankRemover() {
        }

        @Override
        public List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor,
                                         IPluginConfig config) {
            return null;
        }

    }


    class UniDirectionalTankFixer implements ITankProvider {

        private final ForgeDirection directionOverride;
        private final Integer extractSingleTank;

        private UniDirectionalTankFixer() {
            this(ForgeDirection.UNKNOWN, null);
        }

        private UniDirectionalTankFixer(ForgeDirection directionOverride, Integer extractSingleTank) {
            this.directionOverride = directionOverride;
            this.extractSingleTank = extractSingleTank;
        }

        public static UniDirectionalTankFixer withDirectionOverride(ForgeDirection directionOverride) {
            return new UniDirectionalTankFixer(directionOverride, null);
        }

        public static UniDirectionalTankFixer withDirectionOverrideSingleTank(ForgeDirection directionOverride,
                                                                              int index) {
            return new UniDirectionalTankFixer(directionOverride, index);
        }

        @Override
        public List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor,
                                         IPluginConfig config) {
            ILiquidTank[] tanks = container.getTanks(directionOverride);
            if (tanks == null || tanks.length == 0) return Collections.emptyList();
            if (extractSingleTank == null) {
                List<LiquidData> ret = new ArrayList<LiquidData>();
                for (ILiquidTank tank : tanks) {
                    ret.add(new LiquidData(tank));
                }
                return ret;
            }
            if (tanks.length <= extractSingleTank) return Collections.emptyList();
            ILiquidTank tank = tanks[extractSingleTank];
            return Collections.singletonList(new LiquidData(tank));
        }

    }

}
