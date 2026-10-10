package mcp.mobius.waila.addons.bc2;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import mcp.mobius.waila.api.ITankProvider;
import mcp.mobius.waila.api.LiquidData;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.ServerDataAccessorCommon;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.BuildCraftEnergy_oilStill;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getCapacity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidQuantity;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static void writeToNBT(Object container, NBTTagCompound tag) throws Throwable {
        List<LiquidData> tanks = new ArrayList<LiquidData>();

        if (WailaRegistrar.instance().hasTankProviders(container)) {
            bigLoop:
            for (List<ITankProvider> providersList :
                    WailaRegistrar.instance().getTankProviders(container).values()) {
                for (ITankProvider provider : providersList) {
                    List<LiquidData> provided = provider.getTanks(
                            container, ServerDataAccessorCommon.INSTANCE, PluginConfig.instance());
                    if (provided == null) {
                        tanks = null;
                        break bigLoop;
                    } else {
                        tanks.addAll(provided);
                    }
                }
            }
        }

        if (tanks == null)
            tanks = new ArrayList<LiquidData>();
        else if (tanks.isEmpty())
            tanks.addAll(getTanksNaively(container));
        NBTTagList tags = new NBTTagList();
        for (LiquidData tank : tanks) {
            if (tank != null) tags.setTag(tank.toNBT());
        }
        tag.setTag("WailaTanks", tags);
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static List<LiquidData> getTanksNaively(Object container) throws Throwable {
        // First, we collect the tanks...
        Set<Object> tankObjs = new LinkedHashSet<Object>();

        tankObjs.add(new LiquidData(
                BuildCraftEnergy_oilStill != null
                        ? ((Block) BuildCraftEnergy_oilStill.get(null)).blockID
                        : Block.waterStill.blockID,
                (Integer) ILiquidContainer_getLiquidQuantity.invoke(container),
                0,
                (Integer) ILiquidContainer_getCapacity.invoke(container)));

        // ...then we "translate" to LiquidData
        List<LiquidData> ret = new LinkedList<LiquidData>();
        for (Object tank : tankObjs) {
            ret.add(toLiquidData(tank));
        }
        return ret;
    }

    public static LiquidData toLiquidData(Object tank) throws Throwable {
        if (tank instanceof LiquidData) return (LiquidData) tank;
        return null;
    }

}
