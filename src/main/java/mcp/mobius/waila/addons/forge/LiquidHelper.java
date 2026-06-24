package mcp.mobius.waila.addons.forge;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import mcp.mobius.waila.utils.NBTUtil;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

public final class LiquidHelper {

    private static final ForgeDirection[] DIRECTIONS = ForgeDirection.values();

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(ITankContainer container, NBTTagCompound tag) {
        ILiquidTank tank = getTank(container);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;
        new LiquidData(stack, capacity).writeToNBT(tag);
    }

    public static ILiquidTank getTank(ITankContainer container) {
        try {
            for (ForgeDirection dir : DIRECTIONS) {
                ILiquidTank[] tanks = container.getTanks(dir);
                if (tanks != null && tanks.length > 0 && tanks[0] != null)
                    return tanks[0];
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "[Forge] Error trying to access a tank for display!", null);
        }
        return null;
    }

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        return LiquidData.readFromNBT(accessor.getNBTData());
    }

    public static LiquidData getLiquidData(IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof ITankContainer) {
            return LiquidData.readFromNBT(accessor.getNBTData());
        } else if (accessor.getBlock() == Block.cauldron) {
            int meta = accessor.getMetadata();
            LiquidStack stack = new LiquidStack(Block.waterStill, (int) Math.round(Math.min(3, meta) * 333.3));
            return new LiquidData(stack, 1000);
        }

        return new LiquidData(null, 0);
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            LiquidStack stack = data.getLiquidStack();
            if (stack != null) {
                return bar
                        ? TTRenderLiquidBar.create(stack, data.getCapacity())
                        : (stack.amount + "/" + data.getCapacity() + " mB");
            } else {
                return bar ? TTRenderLiquidBar.createEmpty(data.getCapacity()) : null;
            }
        }
        return null;
    }

    public static class LiquidData {

        private final LiquidStack liquidStack;
        private final int capacity;

        public LiquidData(LiquidStack stack, int capacity) {
            this.liquidStack = stack;
            this.capacity = capacity;
        }

        public LiquidStack getLiquidStack() {
            return liquidStack;
        }

        public int getCapacity() {
            return capacity;
        }

        public static LiquidData readFromNBT(NBTTagCompound tag) {
            LiquidStack stack = tag.hasKey("liquidstack")
                    ? LiquidStack.loadLiquidStackFromNBT(tag.getCompoundTag("liquidstack"))
                    : null;
            int capacity = NBTUtil.getNBTInteger(tag, "liquidcapacity");
            return new LiquidData(stack, capacity);
        }

        public void writeToNBT(NBTTagCompound tag) {
            if (liquidStack != null) {
                NBTTagCompound stackNBT = new NBTTagCompound();
                liquidStack.writeToNBT(stackNBT);
                tag.setCompoundTag("liquidstack", stackNBT);
            }
            tag.setInteger("liquidcapacity", capacity);
        }

    }

}
