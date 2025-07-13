package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;


public class HUDHandlerICCharge implements IDataProvider {

    private final Field currCharge;
    private final Field maxCharge;

    public HUDHandlerICCharge(Field currCharge, Field maxCharge) {
        this.currCharge = currCharge;
        this.maxCharge = maxCharge;
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        try {
            tag.a("storage", currCharge.getInt(te));
            if (maxCharge != null)
                tag.a("maxStorage", maxCharge.getInt(te));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass());
        }
    }

}
