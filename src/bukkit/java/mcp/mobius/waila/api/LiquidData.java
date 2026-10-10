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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LiquidData)) return false;
        LiquidData that = (LiquidData) object;
        return getId() == that.getId() && getAmount() == that.getAmount() &&
               getMeta() == that.getMeta() && getCapacity() == that.getCapacity();
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
            NBTTagCompound stackNBT = tag.k("liquidstack");
            id = stackNBT.d("Id");
            amount = stackNBT.e("Amount");
            meta = stackNBT.d("Meta");
        }
        int capacity = NBTUtil.getNBTInteger(tag, "liquidcapacity");
        return new LiquidData(id, amount, meta, capacity);
    }

    public void writeToNBT(NBTTagCompound tag) {
        int id = getId();
        if (id > 0) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stackNBT.a("Id", (short) id);
            stackNBT.a("Amount", amount);
            stackNBT.a("Meta", (short) meta);
            tag.a("liquidstack", stackNBT);
        }
        tag.a("liquidcapacity", capacity);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

}
