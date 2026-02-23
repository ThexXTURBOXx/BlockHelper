package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.IEnergyStorage;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.IEnergyStorage_getCapacity;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.IEnergyStorage_getStored;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileBaseGenerator;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileBaseGenerator_maxStorage;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileBaseGenerator_storage;


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

            if (IEnergyStorage != null && IEnergyStorage.isInstance(te)) {
                storage = (Integer) IEnergyStorage_getStored.invoke(te);
                maxStorage = (Integer) IEnergyStorage_getCapacity.invoke(te);
            } else if (TileBaseGenerator != null && TileBaseGenerator.isInstance(te)) {
                storage = TileBaseGenerator_storage.getShort(te);
                maxStorage = TileBaseGenerator_maxStorage.getShort(te);
            }

            tag.setInt("storage", storage);
            tag.setInt("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
