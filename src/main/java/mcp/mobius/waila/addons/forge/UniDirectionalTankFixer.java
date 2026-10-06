package mcp.mobius.waila.addons.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.api.LiquidData;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public class UniDirectionalTankFixer implements ITankProvider {

    public static final ITankProvider DEFAULT = new UniDirectionalTankFixer();

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

    public static UniDirectionalTankFixer withDirectionOverrideSingleTank(ForgeDirection directionOverride, int index) {
        return new UniDirectionalTankFixer(directionOverride, index);
    }

    @Override
    public List<LiquidData> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config) {
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
