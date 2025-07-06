package mcp.mobius.waila.addons.bc2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

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
            if (BC2Plugin.TileEngine.isInstance(te)) {
                Object engine = BC2Plugin.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BC2Plugin.Engine_energy.getInt(engine);
                    maxsto = BC2Plugin.Engine_maxEnergy.getInt(engine);
                }
            } else if (BC2Plugin.IPowerReceptor.isInstance(te)) {
                Object prov = BC2Plugin.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = BC2Plugin.PowerProvider_energyStored.getInt(prov);
                    maxsto = BC2Plugin.PowerProvider_maxEnergyStored.getInt(prov);
                }
            }

            tag.a("MJEnergy", energy);
            tag.a("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
