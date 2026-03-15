package mcp.mobius.waila.addons.bc3;

import java.util.Map;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getCapacity;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ILiquidTank_getLiquid;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.ITankContainer_getTanks;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidDictionary_liquids;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_amount;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_isLiquidEqual;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemID;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_itemMeta;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_loadLiquidStackFromNBT;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.LiquidStack_writeToNBT;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.newLiquidStack;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We assume {@code container} to be some tank container.
     */
    public static void writeToNBT(Object container, NBTTagCompound tag) throws Throwable {
        Object tank = getTank(container);

        if (ILiquidTank != null && (tank == null || ILiquidTank.isInstance(tank))) {
            Object stack = tank != null ? ILiquidTank_getLiquid.invoke(tank) : null;
            int capacity = tank != null ? (Integer) ILiquidTank_getCapacity.invoke(tank) : 0;

            if (stack != null) {
                NBTTagCompound stackNBT = new NBTTagCompound();
                LiquidStack_writeToNBT.invoke(stack, stackNBT);
                tag.setCompoundTag("liquidstack", stackNBT);
            }
            tag.setInteger("liquidcapacity", capacity);
        }
    }

    /**
     * We assume {@code container} to be some tank container.
     * Returns some liquid tank instance.
     */
    public static Object getTank(Object container) throws Throwable {
        if (ITankContainer != null && ITankContainer.isInstance(container)) {
            Object[] tanks = (Object[]) ITankContainer_getTanks.invoke(container);
            if (tanks != null && tanks.length > 0 && tanks[0] != null)
                return tanks[0];
        }
        return null;
    }

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) throws Throwable {
        NBTTagCompound compound = accessor.getNBTData();
        Object stack = compound.hasKey("liquidstack")
                ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                : null;
        int capacity = accessor.getNBTInteger("liquidcapacity");
        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;
        if (stack != null) {
            id = LiquidStack_itemID.getInt(stack);
            amount = LiquidStack_amount.getInt(stack);
            meta = LiquidStack_itemMeta.getInt(stack);
        }
        return new LiquidData(id, amount, meta, capacity);
    }

    public static LiquidData getLiquidData(IDataAccessor accessor, IPluginConfig config) throws Throwable {
        Object stack;
        int capacity;

        if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            stack = newLiquidStack.newInstance(Block.waterStill.blockID,
                    (int) Math.round(Math.min(3, meta) * 333.3), 0);
            capacity = 1000;
        } else {
            NBTTagCompound compound = accessor.getNBTData();
            stack = compound.hasKey("liquidstack")
                    ? LiquidStack_loadLiquidStackFromNBT.invoke(null, compound.getCompoundTag("liquidstack"))
                    : null;
            capacity = accessor.getNBTInteger("liquidcapacity");
        }

        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;
        if (stack != null) {
            id = LiquidStack_itemID.getInt(stack);
            amount = LiquidStack_amount.getInt(stack);
            meta = LiquidStack_itemMeta.getInt(stack);
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

    @SuppressWarnings("unchecked")
    public static String findLiquidName(LiquidData liquidData) {
        try {
            ItemStack stack = liquidData.asItemStack();
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        if (LiquidDictionary_liquids != null) {
            try {
                Object liquidStack = newLiquidStack.newInstance(liquidData.getId(),
                        liquidData.getAmount(), liquidData.getMeta());
                for (Map.Entry<String, Object> e :
                        ((Map<String, Object>) LiquidDictionary_liquids.get(null)).entrySet()) {
                    try {
                        if ((Boolean) LiquidStack_isLiquidEqual.invoke(liquidStack, e.getValue()))
                            return e.getKey();
                    } catch (Throwable ignored) {
                    }
                }
            } catch (Throwable ignored) {
            }
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
            return new ItemStack(id, 1, meta);
        }

    }

}
