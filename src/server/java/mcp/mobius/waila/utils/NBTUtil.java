package mcp.mobius.waila.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagShort;

public final class NBTUtil {

    private static final Field tagMap;

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    static {
        try {
            tagMap = AccessHelper.getDeclaredField(NBTTagCompound.class, "a", "field_738_a", "tagMap");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutputStream.writeShort(-1);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CompressedStreamTools.writeGzippedCompoundToOutputStream(par0NBTTagCompound, baos);
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
            return CompressedStreamTools.func_770_a(new ByteArrayInputStream(abyte));
        }
    }

    @SuppressWarnings("unchecked")
    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        try {
            NBTBase subtag = ((Map<String, NBTBase>) tagMap.get(tag)).get(keyname);
            if (subtag instanceof NBTTagInt)
                return tag.getInteger(keyname);
            if (subtag instanceof NBTTagShort)
                return tag.getShort(keyname);
            if (subtag instanceof NBTTagByte)
                return tag.getByte(keyname);
            if (subtag instanceof NBTTagFloat)
                return Math.round(tag.getFloat(keyname));
            if (subtag instanceof NBTTagDouble)
                return (int) Math.round(tag.getDouble(keyname));
        } catch (Throwable ignored) {
        }

        return 0;
    }

}
