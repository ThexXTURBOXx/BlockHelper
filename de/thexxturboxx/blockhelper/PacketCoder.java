package de.thexxturboxx.blockhelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

class PacketCoder {

    static Object decode(DataInputStream is) throws IOException {
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
                        Vec3.createVectorHelper(is.readInt(), is.readInt(), is.readInt()));
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
            int c = 0;
            while (c++ < size) {
                length = is.readShort();
                data = new char[length];
                for (int i = 0; i < length; i++) {
                    data[i] = is.readChar();
                }
                pc.add(new String(data));
                c++;
            }
            return pc;
        }
        return new Object();
    }

    static void encode(DataOutputStream os, Object o) throws IOException {
        if (mod_BlockHelper.iof(o, "de.thexxturboxx.blockhelper.PacketInfo")) {
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
                os.writeInt((int) pi.mop.hitVec.xCoord);
                os.writeInt((int) pi.mop.hitVec.yCoord);
                os.writeInt((int) pi.mop.hitVec.zCoord);
            }
        } else if (mod_BlockHelper.iof(o, "java.lang.String")) {
            os.writeByte(1);
            String oa = (String) o;
            os.writeShort(oa.length());
            os.writeChars(oa);
        } else if (mod_BlockHelper.iof(o, "de.thexxturboxx.blockhelper.PacketClient")) {
            os.writeByte(2);
            PacketClient pc = (PacketClient) o;
            os.writeShort(pc.data.size());
            for (String s : pc.data) {
                os.writeShort(s.length());
                os.writeChars(s);
            }
        }
    }

}
