package mcp.mobius.waila.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;

public final class NBTUtil {

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutputStream.writeShort(-1);
        } else {
            byte[] abyte = NBTCompressedStreamTools.a(par0NBTTagCompound);

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
            return NBTCompressedStreamTools.a(abyte);
        }
    }

    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        NBTBase subtag = tag.get(keyname);
        if (subtag == null)
            return 0;
        if (subtag.getTypeId() == 1)
            return tag.getByte(keyname);
        if (subtag.getTypeId() == 2)
            return tag.getShort(keyname);
        if (subtag.getTypeId() == 3)
            return tag.getInt(keyname);
        if (subtag.getTypeId() == 4)
            return (int) tag.getLong(keyname);
        if (subtag.getTypeId() == 5)
            return Math.round(tag.getFloat(keyname));
        if (subtag.getTypeId() == 6)
            return (int) Math.round(tag.getDouble(keyname));

        return 0;
    }

}
