package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getCapacity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidId;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidQuantity;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.ILiquidContainer_getLiquidSlots;
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
            if (ILiquidContainer_getLiquidSlots != null) {
                Object[] slots = (Object[]) ILiquidContainer_getLiquidSlots.invoke(container);
                if (slots != null)
                    for (Object slot : slots) {
                        int capacity = slot == null ? 0 : (Integer) LiquidSlot_getCapacity.invoke(slot);
                        if (capacity > 0)
                            return new LiquidData(
                                    (Integer) LiquidSlot_getLiquidId.invoke(slot),
                                    (Integer) LiquidSlot_getLiquidQty.invoke(slot),
                                    capacity);
                    }
            }

            int quantity = (Integer) ILiquidContainer_getLiquidQuantity.invoke(container);
            int capacity = Math.max(quantity, (Integer) ILiquidContainer_getCapacity.invoke(container));
            if (capacity > 0)
                return new LiquidData((Integer) ILiquidContainer_getLiquidId.invoke(container), quantity, capacity);
        }

        return null;
    }

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        int capacity = accessor.getNBTInteger("liquidcapacity");
        int id = accessor.getNBTData().hasKey("liquidtype")
                ? accessor.getNBTInteger("liquidtype")
                : TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = accessor.getNBTInteger("liquidamt");

        if (id == 0)
            id = TTRenderLiquidBar.EMPTY_LIQUID;

        return new LiquidData(id, amount, capacity);
    }

    public static LiquidData getLiquidData(IDataAccessor accessor, IPluginConfig config) {
        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount;
        int capacity;

        if (accessor.getBlock() == Block.cauldron) {
            id = Block.waterStill.blockID;
            amount = (int) Math.round(Math.min(3, accessor.getMetadata()) * 333.3);
            capacity = 1000;
        } else {
            if (accessor.getNBTData().hasKey("liquidtype"))
                id = accessor.getNBTInteger("liquidtype");
            amount = accessor.getNBTInteger("liquidamt");
            capacity = accessor.getNBTInteger("liquidcapacity");
        }

        if (id == 0)
            id = TTRenderLiquidBar.EMPTY_LIQUID;

        return new LiquidData(id, amount, capacity);
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            if (data.getId() != TTRenderLiquidBar.EMPTY_LIQUID) {
                return bar
                        ? TTRenderLiquidBar.create(data)
                        : (data.getAmount() + "/" + data.getCapacity() + " mB");
            } else {
                return bar ? TTRenderLiquidBar.createEmpty(data.getCapacity()) : null;
            }
        }
        return null;
    }

    public static String findLiquidName(LiquidData liquidData) {
        try {
            ItemStack stack = new ItemStack(liquidData.getId(), 1, 0);
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        return I18n.translate("hud.msg.unknown");
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
