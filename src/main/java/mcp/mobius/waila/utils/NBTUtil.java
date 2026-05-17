package mcp.mobius.waila.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTUtil {

    private static final Field tagMap;

    static {
        try {
            tagMap = AccessHelper.getDeclaredField(NBTTagCompound.class, "tagMap", "field_74784_a", "a");
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
            byte[] abyte = CompressedStreamTools.compress(nbt);

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
            return CompressedStreamTools.decompress(abyte);
        }
    }

    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        NBTBase subtag = tag.getTag(keyname);
        if (subtag == null)
            return 0;
        if (subtag.getId() == 1)
            return tag.getByte(keyname);
        if (subtag.getId() == 2)
            return tag.getShort(keyname);
        if (subtag.getId() == 3)
            return tag.getInteger(keyname);
        if (subtag.getId() == 4)
            return (int) tag.getLong(keyname);
        if (subtag.getId() == 5)
            return Math.round(tag.getFloat(keyname));
        if (subtag.getId() == 6)
            return (int) Math.round(tag.getDouble(keyname));

        return 0;
    }

    @SuppressWarnings("unchecked")
    public static String toString(NBTBase nbt) {
        try {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                StringBuilder sb = new StringBuilder(tag.getName() + ":[");
                for (String key : ((Map<String, ?>) tagMap.get(tag)).keySet()) {
                    sb.append(key).append(":").append(toString(tag.getTag(key))).append(",");
                }
                return sb + "]";
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "NBTUtil#toString", null);
        }
        return nbt.toString();
    }

}
