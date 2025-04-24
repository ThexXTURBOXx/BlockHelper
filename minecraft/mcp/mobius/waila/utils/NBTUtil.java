package mcp.mobius.waila.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;

public final class NBTUtil {

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutputStream.writeShort(-1);
        } else {
            byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);

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
            return CompressedStreamTools.decompress(abyte);
        }
    }

    public static int getNBTInteger(NBTTagCompound tag, String keyname) {
        NBTBase subtag = tag.getTag(keyname);
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

        return 0;
    }

}
