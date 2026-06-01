package mcp.mobius.waila.addons.advmachines.synke;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.addons.advmachines.synke.AdvMachinesSnykePlugin.TileAdvMachine;
import static mcp.mobius.waila.addons.advmachines.synke.AdvMachinesSnykePlugin.TileAdvMachine_energy;
import static mcp.mobius.waila.addons.advmachines.synke.AdvMachinesSnykePlugin.TileAdvMachine_maxEnergy;

public final class HUDHandlerAdvGeneratorSnyke implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerAdvGeneratorSnyke();

    private HUDHandlerAdvGeneratorSnyke() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int storage = -1;
            int maxStorage = -1;

            if (TileAdvMachine.isInstance(te)) {
                storage = TileAdvMachine_energy.getInt(te);
                maxStorage = TileAdvMachine_maxEnergy.getInt(null);
            }

            tag.setInteger("storage", storage);
            tag.setInteger("maxStorage", maxStorage);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
