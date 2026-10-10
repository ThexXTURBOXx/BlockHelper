package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityFurnace;

public final class HUDHandlerFurnace implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFurnace();

    private HUDHandlerFurnace() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) te;
            tag.a("MaxFuel", furnace.b);
        }
    }

}
