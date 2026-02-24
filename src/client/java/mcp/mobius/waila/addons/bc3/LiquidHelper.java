package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

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
                stackNBT.setInteger("Amount", (Integer) LiquidSlot_getLiquidQty.invoke(tank));
                stackNBT.setShort("Meta", (short) 0);
                tag.setCompoundTag("liquidstack", stackNBT);
            }
            tag.setInteger("liquidcapacity", capacity);
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

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        int capacity = accessor.getNBTInteger("liquidcapacity"); // is 0 if not defined
        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;

        NBTTagCompound compound = accessor.getNBTData();
        if (compound.hasKey("liquidstack")) {
            NBTTagCompound stackNBT = compound.getCompoundTag("liquidstack");
            short lId = stackNBT.getShort("Id");
            if (lId != 0) {
                id = lId;
                amount = stackNBT.getInteger("Amount");
                meta = stackNBT.getShort("Meta");
            }
        }

        return new LiquidData(id, amount, meta, capacity);
    }

    public static LiquidData getLiquidData(IDataAccessor accessor, IPluginConfig config) {
        int capacity = accessor.getNBTInteger("liquidcapacity"); // is 0 if not defined
        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;

        NBTTagCompound compound = accessor.getNBTData();
        if (accessor.getBlock() == Block.cauldron) {
            capacity = 1000;
            id = Block.waterStill.blockID;
            meta = accessor.getMetadata();
            amount = (int) Math.round(Math.min(3, meta) * 333.3);
        } else if (compound.hasKey("liquidstack")) {
            NBTTagCompound stackNBT = compound.getCompoundTag("liquidstack");
            short lId = stackNBT.getShort("Id");
            if (lId != 0) {
                id = lId;
                amount = stackNBT.getInteger("Amount");
                meta = stackNBT.getShort("Meta");
            }
        }

        return new LiquidData(id, amount, meta, capacity);
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
            ItemStack stack = liquidData.asItemStack();
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        return I18n.translate("hud.msg.unknown");
    }

    public static class LiquidData {

        private final int id;
        private final int amount;
        private final int meta;
        private final int capacity;

        public LiquidData(int id, int amount, int meta, int capacity) {
            this.id = id;
            this.amount = amount;
            this.meta = meta;
            this.capacity = capacity;
        }

        public int getId() {
            return id;
        }

        public int getAmount() {
            return amount;
        }

        public int getMeta() {
            return meta;
        }

        public int getCapacity() {
            return capacity;
        }

        public ItemStack asItemStack() {
            return new ItemStack(id, amount, meta);
        }

    }

}
