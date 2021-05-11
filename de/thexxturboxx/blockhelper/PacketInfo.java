package de.thexxturboxx.blockhelper;

import net.minecraft.util.MovingObjectPosition;

public class PacketInfo {

    public MovingObjectPosition mop;
    public int dimId;
    public MopType mt;
    public int entityId;

    public PacketInfo(int dimId, MovingObjectPosition mop, MopType mt) {
        this(dimId, mop, mt, -1);
    }

    public PacketInfo(int dimId, MovingObjectPosition mop, MopType mt, int entityId) {
        this.dimId = dimId;
        this.mop = mop;
        this.mt = mt;
        this.entityId = entityId;
    }

}
