package mcp.mobius.waila.api;

import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidData {

    private final LiquidStack liquidStack;
    private final int capacity;

    public LiquidData(ILiquidTank tank) {
        this(tank.getLiquid(), tank.getCapacity());
    }

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

    public LiquidData add(LiquidData that) {
        if (!isSameLiquid(that)) return null;

        LiquidStack thisStack = getLiquidStack();
        LiquidStack thatStack = that.getLiquidStack();
        if (thisStack == null) return new LiquidData(null, getCapacity() + that.getCapacity());

        LiquidStack copy = thisStack.copy();
        copy.amount += thatStack.amount;
        return new LiquidData(copy, getCapacity() + that.getCapacity());
    }

    public boolean isSameLiquid(LiquidData that) {
        LiquidStack thisStack = getLiquidStack();
        LiquidStack thatStack = that.getLiquidStack();
        return thisStack == thatStack || (thisStack != null && thisStack.isLiquidEqual(thatStack));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiquidData)) return false;
        LiquidData that = (LiquidData) o;
        return getCapacity() == that.getCapacity() && isSameLiquid(that);
    }

    @Override
    public int hashCode() {
        LiquidStack stack = getLiquidStack();
        return 31 * (stack == null ? 0 : stack.hashCode()) + getCapacity();
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

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

}
