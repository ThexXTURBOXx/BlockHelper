package mcp.mobius.waila.api;

import net.minecraft.tileentity.TileEntity;

public interface IServerDataAccessor extends IServerCommonAccessor {

    TileEntity getTileEntity();

    int getX();

    int getY();

    int getZ();

}
