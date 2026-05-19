package mcp.mobius.waila.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.server.CompressedStreamTools;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;

public final class NBTUtil {

    private static final Field tagMap;

    static {
        try {
            tagMap = AccessHelper.getDeclaredField(NBTTagCompound.class, "a");
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
            CompressedStreamTools.a(nbt, baos);
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
            return CompressedStreamTools.a(new ByteArrayInputStream(abyte));
        }
    }

    @SuppressWarnings("unchecked")
    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        try {
            NBTBase subtag = ((Map<String, NBTBase>) tagMap.get(tag)).get(keyname);
            if (subtag == null)
                return 0;
            if (subtag.a() == 1)
                return tag.c(keyname);
            if (subtag.a() == 2)
                return tag.d(keyname);
            if (subtag.a() == 3)
                return tag.e(keyname);
            if (subtag.a() == 4)
                return (int) tag.f(keyname);
            if (subtag.a() == 5)
                return Math.round(tag.g(keyname));
            if (subtag.a() == 6)
                return (int) Math.round(tag.h(keyname));
        } catch (Throwable ignored) {
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    public static String toString(NBTBase nbt) {
        try {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                StringBuilder sb = new StringBuilder(tag.b() + ":[");
                for (Map.Entry<String, NBTBase> e : ((Map<String, NBTBase>) tagMap.get(tag)).entrySet()) {
                    sb.append(e.getKey()).append(":").append(toString(e.getValue())).append(",");
                }
                return sb + "]";
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "NBTUtil#toString");
        }
        return nbt.toString();
    }

}
