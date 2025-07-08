package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class HUDHandlerElecMachine implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerElecMachine();

    private HUDHandlerElecMachine() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            if (IC2Plugin.TileBaseGenerator.isInstance(te)) return; // skip, handled elsewhere

            int maxStorage = -1;

            if (IC2Plugin.TileEntityElecMachine.isInstance(te)) {
                maxStorage = IC2Plugin.TileEntityElecMachine_maxEnergy.getInt(te);
            }

            tag.setInteger("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass());
        }
    }

}
