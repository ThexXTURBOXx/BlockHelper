package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

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
            if (BC3Plugin.TileEngine.isInstance(te)) {
                Object engine = BC3Plugin.TileEngine_engine.get(te);
                if (engine != null) {
                    energy = BC3Plugin.Engine_energy.getFloat(engine);
                    maxsto = BC3Plugin.Engine_maxEnergy.getInt(engine);
                }
            } else if (BC3Plugin.IPowerReceptor.isInstance(te)) {
                Object prov = BC3Plugin.IPowerReceptor_getPowerProvider.invoke(te);
                if (prov != null) {
                    energy = (Float) BC3Plugin.IPowerProvider_getEnergyStored.invoke(prov);
                    maxsto = (Integer) BC3Plugin.IPowerProvider_getMaxEnergyStored.invoke(prov);
                }
            }

            tag.setInteger("MJEnergy", Math.round(energy));
            tag.setInteger("MJMaxStorage", maxsto);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
