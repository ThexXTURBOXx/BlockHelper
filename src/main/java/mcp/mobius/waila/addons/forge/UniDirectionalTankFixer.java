package mcp.mobius.waila.addons.forge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.ITankProvider;
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
    public List<ILiquidTank> getTanks(ITankContainer container, IServerCommonAccessor accessor, IPluginConfig config) {
        if (extractSingleTank != null) {
            return Collections.singletonList(container.getTanks(directionOverride)[extractSingleTank]);
        }
        return Arrays.asList(container.getTanks(directionOverride));
    }

}
