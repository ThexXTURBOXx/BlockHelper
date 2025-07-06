package mcp.mobius.waila.addons.advmachines.as;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public final class HUDHandlerAdvGeneratorAS implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvGeneratorAS();

    private HUDHandlerAdvGeneratorAS() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int storage = -1;
            int maxStorage = -1;

            if (AdvMachinesASPlugin.TileEntityBaseMachine.isInstance(te)) {
                storage = AdvMachinesASPlugin.TileEntityBaseMachine_energy.getInt(te);
                maxStorage = AdvMachinesASPlugin.TileEntityBaseMachine_maxEnergy.getInt(te);
            }

            tag.setInt("storage", storage);
            tag.setInt("maxStorage", maxStorage);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
