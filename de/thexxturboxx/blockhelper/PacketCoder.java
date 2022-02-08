package de.thexxturboxx.blockhelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

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
                World w = DimensionManager.getProvider(dimId).worldObj;
                int entityId = is.readInt();
                Entity entity = w.getEntityByID(entityId);
                if (entity != null)
                    mop = new MovingObjectPosition(entity);
                else
                    mop = null;
                return new PacketInfo(dimId, mop, MopType.ENTITY, entityId);
            } else {
                mop = new MovingObjectPosition(is.readInt(), is.readInt(), is.readInt(), is.readInt(),
                        Vec3.createVectorHelper(is.readDouble(), is.readDouble(), is.readDouble()));
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

}
