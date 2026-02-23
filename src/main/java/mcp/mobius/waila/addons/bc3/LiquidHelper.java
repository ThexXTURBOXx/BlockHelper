package mcp.mobius.waila.addons.bc3;

import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidDictionary;
import buildcraft.api.liquids.LiquidStack;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.AccessHelper;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(Object container, NBTTagCompound tag) {
        ILiquidTank tank = getTank((ITankContainer) container);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (stack != null) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stack.writeToNBT(stackNBT);
            tag.setCompoundTag("liquidstack", stackNBT);
        }
        tag.setInteger("liquidcapacity", capacity);
    }

    public static ILiquidTank getTank(ITankContainer container) {
        try {
            ILiquidTank[] tanks = container.getTanks();
            if (tanks != null && tanks.length > 0 && tanks[0] != null)
                return tanks[0];
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[BC3] Unhandled exception trying to access a tank for display!\n", t);
        }
        return null;
    }

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        NBTTagCompound compound = accessor.getNBTData();
        LiquidStack stack = compound.hasKey("liquidstack")
                ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                : null;
        int capacity = accessor.getNBTInteger("liquidcapacity");
        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;
        if (stack != null) {
            id = stack.itemID;
            amount = stack.amount;
            meta = stack.itemMeta;
        }
        return new LiquidData(id, amount, meta, capacity);
    }

    public static LiquidData getLiquidData(IDataAccessor accessor, IPluginConfig config) {
        LiquidStack stack = null;
        int capacity = 0;

        if (accessor.getTileEntity() instanceof ITankContainer) {
            NBTTagCompound compound = accessor.getNBTData();
            stack = compound.hasKey("liquidstack")
                    ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                    : null;
            capacity = accessor.getNBTInteger("liquidcapacity");
        } else if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            stack = new LiquidStack(Block.waterStill, (int) Math.round(Math.min(3, meta) * 333.3));
            capacity = 1000;
        }

        int id = TTRenderLiquidBar.EMPTY_LIQUID;
        int amount = 0;
        int meta = 0;
        if (stack != null) {
            id = stack.itemID;
            amount = stack.amount;
            meta = stack.itemMeta;
        }

        return new LiquidData(id, amount, meta, capacity);
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            if (data.id != TTRenderLiquidBar.EMPTY_LIQUID) {
                return bar
                        ? TTRenderLiquidBar.create(data)
                        : (data.amount + "/" + data.getCapacity() + " mB");
            } else {
                return bar ? TTRenderLiquidBar.createEmpty(data.getCapacity()) : null;
            }
        }
        return null;
    }

    private static Field liquids = null;

    @SuppressWarnings("unchecked")
    public static String findLiquidName(LiquidData liquidData) {
        try {
            ItemStack stack = liquidData.asItemStack();
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        try {
            if (liquids == null)
                liquids = AccessHelper.getDeclaredField(LiquidDictionary.class, "liquids");
            LiquidStack liquidStack = new LiquidStack(liquidData.id, liquidData.amount, liquidData.meta);
            for (String name : ((Map<String, LiquidStack>) liquids.get(null)).keySet()) {
                try {
                    LiquidStack stackOfList = (LiquidStack) liquids.get(name);
                    if (liquidStack.isLiquidEqual(stackOfList))
                        return name;
                } catch (Throwable ignored) {
                }
            }
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
