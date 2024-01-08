package de.thexxturboxx.blockhelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;

public final class PacketCoder {

    private PacketCoder() {
        throw new UnsupportedOperationException();
    }

    public static Object decode(DataInputStream is) throws IOException {
        byte type = is.readByte();
        switch (type) {
        case 0:
            int dimId = is.readInt();
            MopType mt = mod_BlockHelper.MOP_TYPES[is.readInt()];
            MovingObjectPosition mop;
            if (mt == MopType.ENTITY) {
                World w = ModLoader.getMinecraftInstance().theWorld;
                int entityId = is.readInt();
                Entity entity = mod_BlockHelper.getEntityByID(w, entityId);
                if (entity != null)
                    mop = new MovingObjectPosition(entity);
                else
                    mop = null;
                return new PacketInfo(dimId, mop, MopType.ENTITY, entityId);
            } else {
                mop = new MovingObjectPosition(is.readInt(), is.readInt(), is.readInt(), is.readInt(),
                        Vec3D.createVectorHelper(is.readDouble(), is.readDouble(), is.readDouble()));
                return new PacketInfo(dimId, mop, mt);
            }
        case 1:
            short length = is.readShort();
            char[] data = new char[length];
            for (int i = 0; i < length; i++) {
                data[i] = is.readChar();
            }
            return new String(data);
        case 2:
            PacketClient pc = new PacketClient();
            short size = is.readShort();
            for (int c = 0; c < size; c++) {
                length = is.readShort();
                data = new char[length];
                for (int i = 0; i < length; i++) {
                    data[i] = is.readChar();
                }
                pc.add(new String(data));
            }
            return pc;
        }
        return new Object();
    }

    public static void encode(DataOutputStream os, Object o) throws IOException {
        if (o instanceof PacketInfo) {
            PacketInfo pi = (PacketInfo) o;
            os.writeByte(0);
            os.writeInt(pi.dimId);
            os.writeInt(pi.mt.ordinal());
            if (pi.mt == MopType.ENTITY) {
                os.writeInt(pi.entityId);
            } else {
                os.writeInt(pi.mop.blockX);
                os.writeInt(pi.mop.blockY);
                os.writeInt(pi.mop.blockZ);
                os.writeInt(pi.mop.sideHit);
                os.writeDouble(pi.mop.hitVec.xCoord);
                os.writeDouble(pi.mop.hitVec.yCoord);
                os.writeDouble(pi.mop.hitVec.zCoord);
            }
        } else if (o instanceof String) {
            String oa = (String) o;
            os.writeByte(1);
            os.writeShort(oa.length());
            os.writeChars(oa);
        } else if (o instanceof PacketClient) {
            PacketClient pc = (PacketClient) o;
            os.writeByte(2);
            os.writeShort(pc.data.size());
            for (String s : pc) {
                os.writeShort(s.length());
                os.writeChars(s);
            }
        }
    }

    // The next two functions are needed for old MLMP implementations (fixed in 1.1v2).
    // They have a terrible bug where some data does not get read completely.
    // However, that only affects the dataString within Packet230ModLoader.
    // Hence, we can use dataInt without any problems.

    public static int[] toIntArray(String... strings) {
        int totalLength = 1;
        for (String s : strings)
            totalLength += s.length() + 1;

        int[] ret = new int[totalLength];
        int idx = 0;
        ret[idx++] = strings.length;
        for (String s : strings) {
            ret[idx++] = s.length();
            for (int i = 0; i < s.length(); ++i)
                ret[idx++] = s.charAt(i);
        }
        return ret;
    }

    public static String[] toStrings(int... ints) {
        int idx = 0;
        String[] ret = new String[ints[idx++]];
        for (int i = 0; i < ret.length; ++i) {
            int len = ints[idx++];
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < len; ++j)
                str.append((char) ints[idx++]);
            ret[i] = str.toString();
        }
        return ret;
    }

}
