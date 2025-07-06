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

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    static {
        try {
            tagMap = AccessHelper.getDeclaredField(NBTTagCompound.class, "a");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutputStream.writeShort(-1);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CompressedStreamTools.a(par0NBTTagCompound, baos);
            byte[] abyte = baos.toByteArray();

            if (abyte.length > 32000)
                par1DataOutputStream.writeShort(-1);
            else {
                par1DataOutputStream.writeShort((short) abyte.length);
                par1DataOutputStream.write(abyte);
            }
        }
    }

    public static NBTTagCompound readNBTTagCompound(DataInputStream par0DataInputStream) throws IOException {
        short short1 = par0DataInputStream.readShort();

        if (short1 < 0) {
            return null;
        } else {
            byte[] abyte = new byte[short1];
            par0DataInputStream.readFully(abyte);
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

}
