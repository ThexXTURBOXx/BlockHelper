package mcp.mobius.waila.addons.thermalexpansion;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin.TileEnergyCell_Recv;
import static mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin.TileEnergyCell_Send;

public final class HUDHandlerEnergyCell implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerEnergyCell();

    private HUDHandlerEnergyCell() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int recv = TileEnergyCell_Recv.getInt(te);
            int send = TileEnergyCell_Send.getInt(te);
            tag.a("Recv", recv);
            tag.a("Send", send);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
