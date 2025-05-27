package mcp.mobius.waila.api;

import net.minecraft.server.TileEntity;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to
 * the game engine.
 */
public interface IServerDataAccessor extends IServerCommonAccessor {

    TileEntity getTileEntity();

    int getX();

    int getY();

    int getZ();

}
