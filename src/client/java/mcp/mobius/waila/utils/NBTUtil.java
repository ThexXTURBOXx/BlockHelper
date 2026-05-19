package mcp.mobius.waila.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

public final class NBTUtil {

    private static final Field tagMap;

    static {
        try {
            tagMap = AccessHelper.getDeclaredField(NBTTagCompound.class, "tagMap", "field_1094_a", "field_1199", "a");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    public static void writeNBTTagCompound(NBTTagCompound nbt, DataOutputStream target) throws IOException {
        if (nbt == null) {
            target.writeInt(-1);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbt, baos);
            byte[] abyte = baos.toByteArray();

            if (abyte.length > 32000)
                target.writeInt(-1);
            else {
                target.writeInt(abyte.length);
                target.write(abyte);
            }
        }
    }

    public static NBTTagCompound readNBTTagCompound(DataInputStream dat) throws IOException {
        int val = dat.readInt();

        if (val < 0) {
            return null;
        } else {
            byte[] abyte = new byte[val];
            dat.readFully(abyte);
            return CompressedStreamTools.func_1138_a(new ByteArrayInputStream(abyte));
        }
    }

    public static ItemStack readStackFromNBT(NBTTagCompound tag) {
        ItemStack itemstack = new ItemStack(Item.stick);
        itemstack.readFromNBT(tag);
        return itemstack.getItem() == null ? null : itemstack;
    }

    @SuppressWarnings("unchecked")
    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        try {
            NBTBase subtag = ((Map<String, NBTBase>) tagMap.get(tag)).get(keyname);
            if (subtag == null)
                return 0;
            if (subtag.getType() == 1)
                return tag.getByte(keyname);
            if (subtag.getType() == 2)
                return tag.getShort(keyname);
            if (subtag.getType() == 3)
                return tag.getInteger(keyname);
            if (subtag.getType() == 4)
                return (int) tag.getLong(keyname);
            if (subtag.getType() == 5)
                return Math.round(tag.getFloat(keyname));
            if (subtag.getType() == 6)
                return (int) Math.round(tag.getDouble(keyname));
        } catch (Throwable ignored) {
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    public static String toString(NBTBase nbt) {
        try {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                StringBuilder sb = new StringBuilder(tag.getKey() + ":[");
                for (Map.Entry<String, NBTBase> e : ((Map<String, NBTBase>) tagMap.get(tag)).entrySet()) {
                    sb.append(e.getKey()).append(":").append(toString(e.getValue())).append(",");
                }
                return sb + "]";
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "NBTUtil#toString", null);
        }
        return nbt.toString();
    }

}
