package mcp.mobius.waila.api.impl;

import mcp.mobius.waila.api.IFMPAccessor;
import mcp.mobius.waila.utils.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class DataAccessorFMP implements IFMPAccessor {

    public static final DataAccessorFMP INSTANCE = new DataAccessorFMP();

    public String id;
    public World world;
    public EntityPlayer player;
    public MovingObjectPosition mop;
    public Vec3 renderingvec;
    public TileEntity tileEntity;
    public NBTTagCompound partialNBT;
    public double partialFrame;

    public void set(World _world, EntityPlayer _player, MovingObjectPosition _mop, NBTTagCompound _partialNBT,
                    String id) {
        this.set(_world, _player, _mop, _partialNBT, id, null, 0.0);
    }

    public void set(World _world, EntityPlayer _player, MovingObjectPosition _mop, NBTTagCompound _partialNBT,
                    String id, Vec3 renderVec, double partialTicks) {
        this.world = _world;
        this.player = _player;
        this.mop = _mop;
        this.tileEntity = _mop == null ? null : world.getBlockTileEntity(_mop.blockX, _mop.blockY, _mop.blockZ);
        this.partialNBT = _partialNBT;
        this.id = id;
        this.renderingvec = renderVec;
        this.partialFrame = partialTicks;
    }

    @Override
    public void clear() {
        this.set(null, null, null, null, null);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    @Override
    public MovingObjectPosition getPosition() {
        return this.mop;
    }

    @Override
    public NBTTagCompound getNBTData() {
        return this.partialNBT;
    }

    @Override
    public NBTTagCompound getFullNBTData() {
        if (this.tileEntity == null) return null;
        NBTTagCompound tag = new NBTTagCompound();
        this.tileEntity.writeToNBT(tag);
        return tag;
    }

    @Override
    public int getNBTInteger(String keyname) {
        return getNBTInteger(getNBTData(), keyname);
    }

    @Override
    public int getNBTInteger(NBTTagCompound tag, String keyname) {
        return NBTUtil.getNBTInteger(tag, keyname);
    }

    @Override
    public double getPartialFrame() {
        return this.partialFrame;
    }

    @Override
    public Vec3 getRenderingPosition() {
        return this.renderingvec;
    }

    @Override
    public String getID() {
        return this.id;
    }

}
