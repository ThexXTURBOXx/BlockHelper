package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerBC3Tanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC3Tanks();

    private HUDHandlerBC3Tanks() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            LiquidHelper.writeToNBT(te, tag);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
