package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.bc3.BC3Plugin.Engine_energy;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.Engine_maxEnergy;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerReceptor;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.IPowerReceptor_getPowerProvider;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.PowerProvider_energyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.PowerProvider_maxEnergyStored;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.TileEngine;
import static mcp.mobius.waila.addons.bc3.BC3Plugin.TileEngine_engine;

public final class HUDHandlerBC3Energy implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC3Energy();

    private HUDHandlerBC3Energy() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            Float energy = -1f;
            Integer maxsto = -1;
            if (TileEngine.isInstance(te)) {
                Object engine = TileEngine_engine.get(te);
                if (engine != null) {
                    energy = Engine_energy.getFloat(engine);
                    maxsto = Engine_maxEnergy.getInt(engine);
                }
            } else if (IPowerReceptor.isInstance(te)) {
                Object prov = IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = PowerProvider_energyStored.getFloat(prov);
                    maxsto = PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            if (energy != null && maxsto != null) {
                tag.setInt("MJEnergy", Math.round(energy));
                tag.setInt("MJMaxStorage", maxsto);
            }

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
