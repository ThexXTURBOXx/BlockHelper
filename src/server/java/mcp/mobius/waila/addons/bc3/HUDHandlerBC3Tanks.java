package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public final class HUDHandlerBC3Tanks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC3Tanks();

    private HUDHandlerBC3Tanks() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        LiquidHelper.writeToNBT(te, tag);
    }

}
