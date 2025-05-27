package mcp.mobius.waila.addons.bc2;

import buildcraft.api.ILiquidContainer;
import java.util.logging.Level;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(Object container, NBTTagCompound tag) {
        LiquidSlotDelegate slot = LiquidHelper.getTank((ILiquidContainer) container);
        int liquidId = slot != null ? slot.liquidId : 0;
        int liquidAmount = slot != null ? slot.liquidQty : 0;
        int capacity = slot != null ? slot.capacity : 0;

        tag.setInteger("liquidtype", liquidId);
        tag.setInteger("liquidamt", liquidAmount);
        tag.setInteger("liquidcapacity", capacity);
    }

    public static LiquidSlotDelegate getTank(ILiquidContainer container) {
        try {
            int quantity = container.getLiquidQuantity();
            int capacity = Math.max(quantity, container.getCapacity());
            if (capacity > 0)
                return new LiquidSlotDelegate(container.getLiquidId(), quantity, capacity);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[BC2] Unhandled exception trying to access a tank for display!\n", t);
        }

        return null;
    }

    public static class LiquidSlotDelegate {
        private final int liquidId;
        private final int liquidQty;
        private final int capacity;

        public LiquidSlotDelegate(int liquidId, int liquidQty, int capacity) {
            this.liquidId = liquidId;
            this.liquidQty = liquidQty;
            this.capacity = capacity;
        }
    }

}
