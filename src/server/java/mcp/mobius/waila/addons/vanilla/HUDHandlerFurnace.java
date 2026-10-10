package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;

public final class HUDHandlerFurnace implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerFurnace();

    private HUDHandlerFurnace() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) te;
            tag.setInteger("MaxFuel", furnace.currentItemBurnTime);
        }
    }

}
