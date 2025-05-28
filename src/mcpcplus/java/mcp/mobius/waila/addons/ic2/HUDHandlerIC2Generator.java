package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerIC2Generator implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerIC2Generator();

    private HUDHandlerIC2Generator() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            short storage = -1;
            int production = -1;
            short maxStorage = -1;

            if (IC2Plugin.TileBaseGenerator.isInstance(te)) {
                storage = IC2Plugin.TileBaseGenerator_storage.getShort(te);
                production = IC2Plugin.TileBaseGenerator_production.getInt(te);
                maxStorage = IC2Plugin.TileBaseGenerator_maxStorage.getShort(te);
            }

            tag.setShort("storage", storage);
            tag.setInt("production", production);
            tag.setShort("maxStorage", maxStorage);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
