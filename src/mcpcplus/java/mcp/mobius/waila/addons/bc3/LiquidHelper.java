package mcp.mobius.waila.addons.bc3;

import net.minecraft.server.NBTTagCompound;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidQuantity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidContainer_getLiquidSlots;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidId;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidSlot_getLiquidQty;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.newLiquidSlot;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static void writeToNBT(Object container, NBTTagCompound tag) throws Throwable {
        Object tank = getTank(container);

        if (LiquidSlot != null && (tank == null || LiquidSlot.isInstance(tank))) {
            short id = tank != null ? (short) (int) (Integer) LiquidSlot_getLiquidId.invoke(tank) : 0;
            int capacity = tank != null ? (Integer) LiquidSlot_getCapacity.invoke(tank) : 0;

            if (tank != null && id != 0) {
                NBTTagCompound stackNBT = new NBTTagCompound();
                stackNBT.setShort("Id", id);
                stackNBT.setInt("Amount", (Integer) LiquidSlot_getLiquidQty.invoke(tank));
                stackNBT.setShort("Meta", (short) 0);
                tag.set("liquidstack", stackNBT);
            }
            tag.setInt("liquidcapacity", capacity);
            return;
        }
    }

    /**
     * We assume {@code container} to be some tank container.
     * Returns some liquid tank instance.
     */
    public static Object getTank(Object container) throws Throwable {
        if (ILiquidContainer != null && ILiquidContainer.isInstance(container)) {
            Object[] slots = (Object[]) ILiquidContainer_getLiquidSlots.invoke(container);
            if (slots != null && slots.length > 0 && slots[0] != null)
                return slots[0];
            // Capacity not available here in 3.1.5
            return newLiquidSlot.newInstance(
                    ILiquidContainer_getLiquidId.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container),
                    ILiquidContainer_getLiquidQuantity.invoke(container));
        }

        return null;
    }

}
