package mcp.mobius.waila.api.impl;

import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.IServerEntityAccessor;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ServerDataAccessorCommon implements IServerCommonAccessor, IServerDataAccessor, IServerEntityAccessor {

    public World world;
    public EntityPlayerMP player;
    public TileEntity tileEntity;
    public int x, y, z;
    public Entity entity;

    public static final ServerDataAccessorCommon INSTANCE = new ServerDataAccessorCommon();

    public void set(World _world, EntityPlayerMP _player, Entity _entity) {
        this.set(_world, _player, null, 0, 0, 0, _entity);
    }

    public void set(World _world, EntityPlayerMP _player, TileEntity _tileEntity, int _x, int _y, int _z) {
        this.set(_world, _player, _tileEntity, x, y, z, null);
    }

    public void set(World _world, EntityPlayerMP _player,
                    TileEntity _tileEntity, int _x, int _y, int _z, Entity _entity) {
        this.world = _world;
        this.player = _player;
        this.tileEntity = _tileEntity;
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.entity = _entity;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public EntityPlayerMP getPlayer() {
        return this.player;
    }

    @Override
    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

}
