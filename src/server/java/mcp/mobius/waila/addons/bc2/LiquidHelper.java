package mcp.mobius.waila.addons.bc2;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.BuildCraftEnergy_oilStill;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer;
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
        LiquidData slot = getTank(container);
        int liquidId = slot != null ? slot.getId() : 0;
        int liquidAmount = slot != null ? slot.getAmount() : 0;
        int capacity = slot != null ? slot.getCapacity() : 0;

        tag.setInteger("liquidtype", liquidId);
        tag.setInteger("liquidamt", liquidAmount);
        tag.setInteger("liquidcapacity", capacity);
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static LiquidData getTank(Object container) throws Throwable {
        if (ILiquidContainer != null && ILiquidContainer.isInstance(container)) {
            int quantity = (Integer) ILiquidContainer_getLiquidQuantity.invoke(container);
            int capacity = Math.max(quantity, (Integer) ILiquidContainer_getCapacity.invoke(container));
            if (capacity > 0)
                return new LiquidData(BuildCraftEnergy_oilStill != null
                        ? ((Block) BuildCraftEnergy_oilStill.get(null)).blockID
                        : Block.waterStill.blockID,
                        quantity, capacity);
        }

        return null;
    }

    public static class LiquidData {

        private final int id;
        private final int amount;
        private final int capacity;

        public LiquidData(int id, int amount, int capacity) {
            this.id = id;
            this.amount = amount;
            this.capacity = capacity;
        }

        public int getId() {
            return id;
        }

        public int getAmount() {
            return amount;
        }

        public int getCapacity() {
            return capacity;
        }

        public ItemStack asItemStack() {
            return new ItemStack(id, 1, 0);
        }

    }

}
