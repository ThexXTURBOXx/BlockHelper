package mcp.mobius.waila.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

public final class NBTUtil {

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutputStream.writeShort(-1);
        } else {
            byte[] abyte = CompressedStreamTools.func_40591_a(par0NBTTagCompound);

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
            return CompressedStreamTools.func_40592_a(abyte);
        }
    }

    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        NBTBase subtag = tag.func_40196_b(keyname);
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

        return 0;
    }

}
