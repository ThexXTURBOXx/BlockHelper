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
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getCapacity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidId;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidQuantity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidSlots;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.LiquidSlot;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.LiquidSlot_getCapacity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.LiquidSlot_getLiquidId;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.LiquidSlot_getLiquidQty;

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
            if (tank != null) tags.add(tank.toNBT());
        }
        tag.set("WailaTanks", tags);
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static List<LiquidData> getTanksNaively(Object container) throws Throwable {
        // First, we collect the tanks...
        Set<Object> tankObjs = new LinkedHashSet<Object>();

        boolean added = false;
        if (ILiquidContainer_getLiquidSlots != null) {
            Object[] slots = (Object[]) ILiquidContainer_getLiquidSlots.invoke(container);

            if (slots != null) {
                for (Object slot : slots) {
                    if (slot != null) {
                        tankObjs.add(new LiquidData(
                                (Integer) LiquidSlot_getLiquidId.invoke(slot),
                                (Integer) LiquidSlot_getLiquidQty.invoke(slot),
                                0,
                                (Integer) LiquidSlot_getCapacity.invoke(slot)));
                        added = true;
                    }
                }
            }
        }

        if (!added) {
            tankObjs.add(new LiquidData(
                    (Integer) ILiquidContainer_getLiquidId.invoke(container),
                    (Integer) ILiquidContainer_getLiquidQuantity.invoke(container),
                    0,
                    (Integer) ILiquidContainer_getCapacity.invoke(container)));
        }

        // ...then we "translate" to LiquidData
        List<LiquidData> ret = new LinkedList<LiquidData>();
        for (Object tank : tankObjs) {
            ret.add(toLiquidData(tank));
        }
        return ret;
    }

    public static LiquidData toLiquidData(Object tank) throws Throwable {
        if (tank instanceof LiquidData) return (LiquidData) tank;
        if (LiquidSlot != null && LiquidSlot.isInstance(tank)) {
            int id = (Integer) LiquidSlot_getLiquidId.invoke(tank);
            if (id <= 0) id = LiquidData.EMPTY_LIQUID;
            int amount = (Integer) LiquidSlot_getLiquidQty.invoke(tank);
            int capacity = (Integer) LiquidSlot_getCapacity.invoke(tank);
            return new LiquidData(id, amount, 0, capacity);
        }
        return null;
    }

}
