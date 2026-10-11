package mcp.mobius.waila.api;

import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class LiquidData {

    public static final int EMPTY_LIQUID = -1;

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

    public LiquidData add(LiquidData that) {
        if (!isSameLiquid(that)) return null;
        return new LiquidData(getId(), getAmount() + that.getAmount(), getMeta(),
                getCapacity() + that.getCapacity());
    }

    public boolean isSameLiquid(LiquidData that) {
        return getId() == that.getId() && getMeta() == that.getMeta();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiquidData)) return false;
        LiquidData that = (LiquidData) o;
        return getCapacity() == that.getCapacity() && isSameLiquid(that);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (Object element : new int[]{getId(), getAmount(), getMeta(), getCapacity()})
            hash = 31 * hash + element.hashCode();
        return hash;
    }

    public static LiquidData readFromNBT(NBTTagCompound tag) {
        int id = EMPTY_LIQUID, amount = 0, meta = 0;
        if (tag.hasKey("liquidstack")) {
            NBTTagCompound stackNBT = tag.getCompound("liquidstack");
            id = stackNBT.getShort("Id");
            amount = stackNBT.getInt("Amount");
            meta = stackNBT.getShort("Meta");
        }
        int capacity = NBTUtil.getNBTInteger(tag, "liquidcapacity");
        return new LiquidData(id, amount, meta, capacity);
    }

    public void writeToNBT(NBTTagCompound tag) {
        int id = getId();
        if (id > 0) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stackNBT.setShort("Id", (short) id);
            stackNBT.setInt("Amount", amount);
            stackNBT.setShort("Meta", (short) meta);
            tag.setCompound("liquidstack", stackNBT);
        }
        tag.setInt("liquidcapacity", capacity);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

}
