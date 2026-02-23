package mcp.mobius.waila.addons.bc3;

import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerEntityAccessor;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;

public final class HUDHandlerEntityBC3Tanks implements IEntityProvider {

    public static final IEntityProvider INSTANCE = new HUDHandlerEntityBC3Tanks();

    private HUDHandlerEntityBC3Tanks() {
    }

    @Override
    public void appendServerData(Entity ent, NBTTagCompound tag,
                                 IServerEntityAccessor accessor, IPluginConfig config) {
        try {
            LiquidHelper.writeToNBT(ent, tag);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getEntity().getClass());
        }
    }

}
