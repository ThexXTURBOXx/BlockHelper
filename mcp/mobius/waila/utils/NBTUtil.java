package mcp.mobius.waila.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class NBTUtil {

    private NBTUtil() {
        throw new UnsupportedOperationException();
    }

    public static NBTBase getTag(String key, NBTTagCompound tag) {
        String[] path = key.split("\\.");

        NBTTagCompound deepTag = tag;
        for (String i : path) {
            if (deepTag.hasKey(i)) {
                if (deepTag.getTag(i) instanceof NBTTagCompound)
                    deepTag = deepTag.getCompoundTag(i);
                else {
                    return deepTag.getTag(i);
                }
            } else {
                //Waila.log.log(Level.WARNING, "Leaf " + key + " not found.");
                return null;
            }
        }
        return deepTag;
    }

    public static void setTag(String key, NBTTagCompound targetTag, NBTBase addedTag) {
        String[] path = key.split("\\.");

        NBTTagCompound deepTag = targetTag;
        for (int i = 0; i < path.length - 1; i++) {
            if (!deepTag.hasKey(path[i]))
                deepTag.setTag(path[i], new NBTTagCompound());

            deepTag = deepTag.getCompoundTag(path[i]);
        }

        deepTag.setTag(path[path.length - 1], addedTag);
    }

    public static NBTTagCompound createTag(NBTTagCompound inTag, Set<String> keys) {
        if (keys.contains("*")) return inTag;

        NBTTagCompound outTag = new NBTTagCompound();

        for (String key : keys) {
            NBTBase tagToAdd = getTag(key, inTag);
            if (tagToAdd != null)
                setTag(key, outTag, tagToAdd);
        }

        return outTag;
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

    public static void appendServerData(IDataProvider provider, TileEntity entity, NBTTagCompound tag,
                                        World world, int x, int y, int z) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method appendServerData = provider.getClass().getMethod("appendServerData",
                TileEntity.class, NBTTagCompound.class, World.class, int.class, int.class, int.class);
        appendServerData.invoke(provider, entity, tag, world, x, y, z);
    }

    public static void appendServerData(IEntityProvider provider, Entity entity, NBTTagCompound tag) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method appendServerData = provider.getClass().getMethod("appendServerData",
                Entity.class, NBTTagCompound.class);
        appendServerData.invoke(provider, entity, tag);
    }

}
