package mcp.mobius.waila.addons.bc3;

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
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidQuantity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidSlots;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getLiquid;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer_getTanks;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidQty;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_amount;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemID;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemMeta;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.newLiquidSlot;

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
            if (tank != null) tags.appendTag(tank.toNBT());
        }
        tag.setTag("WailaTanks", tags);
    }

    /**
     * We assume {@code container} to be some tank container.
     * Returns some liquid tank instance.
     */
    public static List<LiquidData> getTanksNaively(Object container) throws Throwable {
        // First, we collect the tanks...
        Set<Object> tankObjs = new LinkedHashSet<Object>();

        if (ITankContainer != null && ITankContainer.isInstance(container)) {
            Object[] tanks = (Object[]) ITankContainer_getTanks.invoke(container);
            if (tanks != null) {
                for (Object tank : tanks) {
                    if (tank != null) {
                        tankObjs.add(tank);
                    }
                }
            }
        }

        if (ILiquidContainer != null && ILiquidContainer.isInstance(container)) {
            boolean added = false;
            if (ILiquidContainer_getLiquidSlots != null) {
                Object[] slots = (Object[]) ILiquidContainer_getLiquidSlots.invoke(container);

                if (slots != null) {
                    for (Object slot : slots) {
                        if (slot != null) {
                            tankObjs.add(slot);
                            added = true;
                        }
                    }
                }
            }

            // Capacity not available here in 3.1.5
            if (!added) tankObjs.add(newLiquidSlot.newInstance(
                    ILiquidContainer_getLiquidId.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container)));
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
        if (ILiquidTank != null && ILiquidTank.isInstance(tank)) {
            Object stack = ILiquidTank_getLiquid.invoke(tank);
            int id = stack != null ? LiquidStack_itemID.getInt(stack) : LiquidData.EMPTY_LIQUID;
            int amount = stack != null ? LiquidStack_amount.getInt(stack) : 0;
            int meta = stack != null ? LiquidStack_itemMeta.getInt(stack) : 0;
            int capacity = (Integer) ILiquidTank_getCapacity.invoke(tank);
            return new LiquidData(id, amount, meta, capacity);
        }
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
