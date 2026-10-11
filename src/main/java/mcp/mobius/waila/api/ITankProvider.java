package mcp.mobius.waila.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.addons.forge.LiquidHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public interface ITankProvider {

    /**
     * Simple {@link ITankProvider} that removes all tank information.
     */
    ITankProvider REMOVER = new TankRemover();

    /**
     * {@link ITankProvider} that sums up all tank information from all faces.
     */
    ITankProvider ACCUMULATOR = new TankAccumulator();

    /**
     * {@link ITankProvider} that sums up all tank information from all faces.
     * Additionally, contracts empty tanks by adding their capacities to the first found non-empty tank.
     */
    ITankProvider CONTRACTING_ACCUMULATOR = new TankAccumulator(true);

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


    class TankAccumulator implements ITankProvider {

        private final boolean contractEmpty;

        private TankAccumulator() {
            this(false);
        }

        private TankAccumulator(boolean contractEmpty) {
            this.contractEmpty = contractEmpty;
        }

        @Override
        public List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor,
                                         IPluginConfig config) {
            List<LiquidData> ret = new ArrayList<LiquidData>();

            int emptyCapacity = 0;
            for (LiquidData data : LiquidHelper.getTanksNaively(container)) {
                if (data != null) {
                    if (data.getLiquidStack() == null ||
                        data.getLiquidStack().itemID == TTRenderLiquidBar.EMPTY_LIQUID) {
                        emptyCapacity += data.getCapacity();
                        continue;
                    }

                    boolean added = false;
                    for (int i = 0; i < ret.size(); i++) {
                        LiquidData r = ret.get(i);
                        LiquidData sum = r.add(data);
                        if (sum != null) {
                            ret.set(i, sum);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        ret.add(data);
                    }
                }
            }

            if (contractEmpty) {
                if (!ret.isEmpty()) {
                    LiquidData old = ret.get(0);
                    ret.set(0, new LiquidData(old.getLiquidStack(), old.getCapacity() + emptyCapacity));
                } else {
                    ret.add(new LiquidData(null, emptyCapacity));
                }
            } else {
                ret.add(new LiquidData(null, emptyCapacity));
            }

            return ret;
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
