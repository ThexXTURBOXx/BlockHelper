package mcp.mobius.waila.addons.forge;

import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderLiquidBar;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public final class LiquidHelper {

    private static final ForgeDirection[] DIRECTIONS = ForgeDirection.values();

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(ITankContainer container, NBTTagCompound tag) {
        ILiquidTank tank = LiquidHelper.getTank(container);
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
            for (ForgeDirection dir : DIRECTIONS) {
                ILiquidTank[] tanks = container.getTanks(dir);
                if (tanks != null && tanks.length > 0 && tanks[0] != null)
                    return tanks[0];
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[Forge] Unhandled exception trying to access a tank for display!\n", t);
        }
        return null;
    }

    public static LiquidData getLiquidData(IEntityAccessor accessor, IPluginConfig config) {
        NBTTagCompound compound = accessor.getNBTData();
        LiquidStack stack = compound.hasKey("liquidstack")
                ? LiquidStack.loadLiquidStackFromNBT(compound.getCompoundTag("liquidstack"))
                : null;
        int capacity = accessor.getNBTInteger("liquidcapacity");
        return new LiquidData(stack, capacity);
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
            stack = meta == 0 ? null : new LiquidStack(Block.waterStill, Math.min(4, meta) * 250);
            capacity = 1000;
        }

        return new LiquidData(stack, capacity);
    }

    public static String getLiquidTooltip(LiquidData data, boolean bar) {
        if (data.getCapacity() > 0) {
            LiquidStack stack = data.getLiquidStack();
            if (stack != null && stack.amount > 0) {
                return bar
                        ? SpecialChars.getRenderString("waila.liquid",
                        findLiquidName(stack),
                        DisplayUtil.itemDisplayNameShort(stack.asItemStack()),
                        stack.amount, data.getCapacity())
                        : (stack.amount + "/" + data.getCapacity() + " mB");
            } else {
                return bar
                        ? SpecialChars.getRenderString("waila.liquid",
                        TTRenderLiquidBar.EMPTY_LIQUID, TTRenderLiquidBar.EMPTY_LIQUID, 0, data.getCapacity())
                        : null;
            }
        }
        return null;
    }

    public static String findLiquidName(LiquidStack reference) {
        if (reference == null) return null;
        Map<String, LiquidStack> liquids = LiquidDictionary.getLiquids();
        for (Map.Entry<String, LiquidStack> e : liquids.entrySet()) {
            LiquidStack stack = e.getValue();
            if (stack != null && stack.isLiquidEqual(reference)) return e.getKey();
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

    }

}
