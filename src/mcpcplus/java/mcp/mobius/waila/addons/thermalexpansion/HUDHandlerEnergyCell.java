package mcp.mobius.waila.addons.thermalexpansion;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerEnergyCell implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerEnergyCell();

    private HUDHandlerEnergyCell() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int recv = ThermalExpansionPlugin.TileEnergyCell_Recv.getInt(te);
            int send = ThermalExpansionPlugin.TileEnergyCell_Send.getInt(te);
            tag.setInt("Recv", recv);
            tag.setInt("Send", send);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
