package de.thexxturboxx.blockhelper;

import net.minecraft.util.MovingObjectPosition;

class PacketInfo {

	MovingObjectPosition mop;
	int dimId;
	MopType mt;
	public int entityId;

	PacketInfo(int dimId, MovingObjectPosition mop, MopType mt) {
		this(dimId, mop, mt, -1);
	}

	PacketInfo(int dimId, MovingObjectPosition mop, MopType mt, int entityId) {
		this.dimId = dimId;
		this.mop = mop;
		this.mt = mt;
		this.entityId = entityId;
	}

}
