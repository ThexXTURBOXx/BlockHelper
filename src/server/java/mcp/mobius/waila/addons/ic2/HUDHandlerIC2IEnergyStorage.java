package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;


public class HUDHandlerIC2IEnergyStorage implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerIC2IEnergyStorage();

    private HUDHandlerIC2IEnergyStorage() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int storage = -1;
            int maxStorage = -1;

            if (IC2Plugin.IEnergyStorage.isInstance(te)) {
                storage = (Integer) IC2Plugin.IEnergyStorage_getStored.invoke(te);
                maxStorage = (Integer) IC2Plugin.IEnergyStorage_getCapacity.invoke(te);
            } else if (IC2Plugin.TileBaseGenerator.isInstance(te)) {
                storage = IC2Plugin.TileBaseGenerator_storage.getShort(te);
                maxStorage = IC2Plugin.TileBaseGenerator_maxStorage.getShort(te);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
