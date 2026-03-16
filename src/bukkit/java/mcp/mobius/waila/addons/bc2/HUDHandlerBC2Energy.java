package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

import static mcp.mobius.waila.addons.bc2.BC2Plugin.Engine_energy;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.Engine_maxEnergy;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.IPowerReceptor;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.IPowerReceptor_getPowerProvider;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.PowerProvider_energyStored;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.PowerProvider_maxEnergyStored;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.TileEngine;
import static mcp.mobius.waila.addons.bc2.BC2Plugin.TileEngine_engine;

public final class HUDHandlerBC2Energy implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBC2Energy();

    private HUDHandlerBC2Energy() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int energy = -1;
            int maxsto = -1;
            if (TileEngine.isInstance(te)) {
                Object engine = TileEngine_engine.get(te);
                if (engine != null) {
                    energy = Engine_energy.getInt(engine);
                    maxsto = Engine_maxEnergy.getInt(engine);
                }
            } else if (IPowerReceptor.isInstance(te)) {
                Object prov = IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = PowerProvider_energyStored.getInt(prov);
                    maxsto = PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            tag.a("MJEnergy", energy);
            tag.a("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
