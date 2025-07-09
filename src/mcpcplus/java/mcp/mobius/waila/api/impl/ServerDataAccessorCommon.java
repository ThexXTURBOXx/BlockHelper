package mcp.mobius.waila.api.impl;

import mcp.mobius.waila.api.IServerCommonAccessor;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.IServerEntityAccessor;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class ServerDataAccessorCommon implements IServerCommonAccessor, IServerDataAccessor, IServerEntityAccessor {

    public World world;
    public EntityPlayer player;
    public TileEntity tileEntity;
    public int x, y, z;
    public Entity entity;

    public static final ServerDataAccessorCommon INSTANCE = new ServerDataAccessorCommon();

    public void set(World _world, EntityPlayer _player, Entity _entity) {
        this.set(_world, _player, null, 0, 0, 0, _entity);
    }

    public void set(World _world, EntityPlayer _player, TileEntity _tileEntity, int _x, int _y, int _z) {
        this.set(_world, _player, _tileEntity, x, y, z, null);
    }

    public void set(World _world, EntityPlayer _player,
                    TileEntity _tileEntity, int _x, int _y, int _z, Entity _entity) {
        this.world = _world;
        this.player = _player;
        this.tileEntity = _tileEntity;
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.entity = _entity;
    }

    public void clear() {
        this.set(null, null, null);
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
