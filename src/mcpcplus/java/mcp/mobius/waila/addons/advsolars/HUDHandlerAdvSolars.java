package mcp.mobius.waila.addons.advsolars;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin.TileEntitySolarPanel;
import static mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin.TileEntitySolarPanel_maxStorage;
import static mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin.TileEntitySolarPanel_storage;

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

            if (TileEntitySolarPanel.isInstance(te)) {
                storage = TileEntitySolarPanel_storage.getInt(te);
                maxStorage = TileEntitySolarPanel_maxStorage.getInt(te);
            }

            tag.setInt("storage", storage);
            tag.setInt("maxStorage", maxStorage);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
