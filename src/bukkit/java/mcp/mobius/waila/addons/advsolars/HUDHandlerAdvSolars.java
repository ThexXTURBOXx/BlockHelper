package mcp.mobius.waila.addons.advsolars;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerAdvSolars implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvSolars();

    private HUDHandlerAdvSolars() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int storage = -1;
            int maxStorage = -1;

            if (AdvSolarsPlugin.TileEntitySolarPanel.isInstance(te)) {
                storage = AdvSolarsPlugin.TileEntitySolarPanel_storage.getInt(te);
                maxStorage = AdvSolarsPlugin.TileEntitySolarPanel_maxStorage.getInt(te);
            }

            tag.a("storage", storage);
            tag.a("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        try {
            int production = -1;
            int maxPacketSize = -1;

            if (AdvSolarsPlugin.TileEntityQGenerator.isInstance(te)) {
                production = AdvSolarsPlugin.TileEntityQGenerator_production.getInt(te);
                maxPacketSize = AdvSolarsPlugin.TileEntityQGenerator_maxPacketSize.getInt(te);
            }

            tag.a("production", production);
            tag.a("maxPacketSize", maxPacketSize);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
