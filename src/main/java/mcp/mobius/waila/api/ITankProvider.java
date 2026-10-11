package mcp.mobius.waila.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.addons.bc3.BC3Plugin;
import mcp.mobius.waila.addons.bc3.LiquidHelper;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.WailaExceptionHandler;

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
     * @see UniDirectionalTankFixer#singleTank(int) This for specifying the tank sub-index to display.
     */
    ITankProvider UNIDIRECTIONAL = new UniDirectionalTankFixer(0);

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


    class TankAccumulator implements ITankProvider {

        private final boolean contractEmpty;

        private TankAccumulator() {
            this(false);
        }

        private TankAccumulator(boolean contractEmpty) {
            this.contractEmpty = contractEmpty;
        }

        @Override
        public List<LiquidData> getTanks(Object container, IServerCommonAccessor accessor,
                                         IPluginConfig config) {
            List<LiquidData> ret = new ArrayList<LiquidData>();

            try {
                int emptyCapacity = 0;
                for (LiquidData data : LiquidHelper.getTanksNaively(container)) {
                    if (data != null) {
                        if (data.getId() == TTRenderLiquidBar.EMPTY_LIQUID) {
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
                        ret.set(0, new LiquidData(old.getId(), old.getAmount(), old.getMeta(),
                                old.getCapacity() + emptyCapacity));
                    } else {
                        ret.add(new LiquidData(TTRenderLiquidBar.EMPTY_LIQUID, 0, 0, emptyCapacity));
                    }
                } else {
                    ret.add(new LiquidData(TTRenderLiquidBar.EMPTY_LIQUID, 0, 0, emptyCapacity));
                }
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, container.getClass(), null);
            }

            return ret;
        }

    }


    class UniDirectionalTankFixer implements ITankProvider {

        private final int extractSingleTank;

        private UniDirectionalTankFixer(int extractSingleTank) {
            this.extractSingleTank = extractSingleTank;
        }

        public static UniDirectionalTankFixer singleTank(int index) {
            return new UniDirectionalTankFixer(index);
        }

        @Override
        public List<LiquidData> getTanks(Object container, IServerCommonAccessor accessor, IPluginConfig config) {
            try {
                if (BC3Plugin.ITankContainer != null && BC3Plugin.ITankContainer_getTanks != null &&
                    BC3Plugin.ITankContainer.isInstance(container)) {
                    Object[] tanks = (Object[]) BC3Plugin.ITankContainer_getTanks.invoke(container);
                    if (tanks == null || tanks.length == 0) return Collections.emptyList();
                    if (tanks.length <= extractSingleTank) return Collections.emptyList();
                    return Collections.singletonList(LiquidHelper.toLiquidData(tanks[extractSingleTank]));
                }
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, container.getClass(), null);
            }
            return Collections.emptyList();
        }

    }

}
