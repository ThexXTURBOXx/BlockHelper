package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerBC2Tanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC2Tanks();

    private HUDHandlerBC2Tanks() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT(te, tag);
    }

}
