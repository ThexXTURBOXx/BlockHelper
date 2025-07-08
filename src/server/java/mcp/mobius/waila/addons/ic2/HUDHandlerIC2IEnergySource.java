package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class HUDHandlerIC2IEnergySource implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerIC2IEnergySource();

    private HUDHandlerIC2IEnergySource() {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            int out = -1;

            if (IC2Plugin.IEnergySource.isInstance(te)) {
                out = (Integer) IC2Plugin.IEnergySource_getOutput.invoke(te);
            }

            tag.setInteger("maxOutput", out);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
