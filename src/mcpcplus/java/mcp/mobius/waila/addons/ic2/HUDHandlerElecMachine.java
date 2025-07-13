package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileBaseGenerator;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityElecMachine;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityElecMachine_maxEnergy;

public class HUDHandlerElecMachine implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerElecMachine();

    private HUDHandlerElecMachine() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            if (TileBaseGenerator.isInstance(te)) return; // skip, handled elsewhere

            int maxStorage = -1;

            if (TileEntityElecMachine.isInstance(te)) {
                maxStorage = TileEntityElecMachine_maxEnergy.getInt(te);
            }

            tag.setInt("maxStorage", maxStorage);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, te.getClass());
        }
    }

}
